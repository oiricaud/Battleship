package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.R.attr.width;
import static edu.utep.cs.cs4330.battleship.R.attr.height;

/**
 * A special view class to display a battleship board as a2D grid.
 *
 * @see Board
 */
public class BoardView extends View {
    private int[][] allBoats = new int[10][10];

    private LinkedList<Integer> xHit = new LinkedList<>();
    private LinkedList<Integer> yHit = new LinkedList<>();

    private LinkedList<Integer> setxMiss = new LinkedList<>();
    private LinkedList<Integer> setyMiss = new LinkedList<>();

    private boolean iShot;

    public boolean iShot() {
        return iShot;
    }

    public void setiShot(boolean iShot) {
        this.iShot = iShot;
    }

    public int[][] getAllBoats() {
        return allBoats;
    }

    public void setAllBoats(int[][] allBoats) {
        this.allBoats = allBoats;
    }

    public LinkedList<Integer> getxHit() {
        return xHit;
    }

    public void setxHit(int i) {
        xHit.add(i);
    }

    public LinkedList<Integer> getyHit() {
        return yHit;
    }
    public void setyHit(int i) {
        yHit.add(i);
    }

    public LinkedList<Integer> getxMiss() {
        return setxMiss;
    }

    public void setxMiss(int i) {
        setxMiss.add(i);
    }

    public LinkedList<Integer> getyMiss() {
        return setyMiss;
    }

    public void setyMiss(int i) {
         setyMiss.add(i);
    }

    /** Callback interface to listen for board touches. */
    public interface BoardTouchListener {

        /**
         * Called when a place of the board is touched.
         * The coordinate of the touched place is provided.
         *
         * @param x 0-based column index of the touched place
         * @param y 0-based row index of the touched place
         */
        void onTouch(int x, int y);
    }

    /** Listeners to be notified upon board touches. */
    private final List<BoardTouchListener> listeners = new ArrayList<>();

    /** Board background color. */
    private final int boardColor = Color.argb(0, 255, 255, 255); // Transparent

    /** Red color circle **/
    private final int redColor = Color.rgb(255,69,0);

    /** White color circle **/
    private final int whiteColor = Color.rgb(255,255,255);

    /** Board background paint. */
    private final Paint boardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        boardPaint.setColor(boardColor);
    }
    /** Red background paint */
    private final Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        redPaint.setColor(redColor);
    }
    /** White background paint */
    private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        whitePaint.setColor(whiteColor);
    }
    /** Board grid line color. */
    private final int boardLineColor = Color.WHITE;

    /** Board grid line paint. */
    private final Paint boardLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        boardLinePaint.setColor(boardLineColor);
        boardLinePaint.setStrokeWidth(3);
    }

    /** Board to be displayed by this view. */
    private Board board;

    /** Size of the board. */
    private int boardSize;

    /** Create a new board view to be run in the given context. */
    public BoardView(Context context) {
        super(context);
    }

    /** Create a new board view with the given attribute set. */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Create a new board view with the given attribute set and style. */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Set the board to to be displayed by this view. */
    public void setBoard(Board board) {
        this.board = board;
        this.boardSize = board.size();
    }

    /**
     * Overridden here to detect a board touch. When the board is
     * touched, the corresponding place is identified,
     * and registered listeners are notified.
     *
     * @see BoardTouchListener
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int xy = locatePlace(event.getX(), event.getY());
                invalidate();
                if (xy >= 0) {
                    Log.w("x eventGetX ", String.valueOf(event.getX()));
                    Log.w("y eventGetY", String.valueOf(event.getY()));
                    notifyBoardTouch(xy / 100, xy % 100);
                }
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /** Overridden here to draw a 2-D representation of the board. */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas); // Draw blue
        drawPlaces(canvas); // Draw red
    }

    /** Draw all the places of the board. */
    private void drawPlaces(Canvas canvas) {

        for(int i = 0 ; i < getxMiss().size(); i++){
            float xMiss = (getxMiss().get(i) * lineGap()) + (lineGap()/2);
            float yMiss = (getyMiss().get(i) * lineGap()) + (lineGap()/2);
            canvas.drawCircle(xMiss, yMiss, (lineGap() / 2), whitePaint);
        }

        for(int j = 0 ; j < getxHit().size(); j++){
            float xHit = (getxHit().get(j) * lineGap()) + (lineGap()/2);
            float yHit = (getyHit().get(j) * lineGap()) + (lineGap() / 2);
            canvas.drawCircle(xHit, yHit, (lineGap() / 2    ), redPaint);
        }
    }


    /** Draw horizontal and vertical lines. */
    private void drawGrid(Canvas canvas) {
        Log.w(" 2)BoardView", "draswGrid(Canvas canvas)");
        final float maxCoord = maxCoord();
        final float placeSize = lineGap();
        canvas.drawRect(0, 0, maxCoord, maxCoord, boardPaint);
        for (int i = 0; i < numOfLines(); i++) {
            float xy = i * placeSize;
            canvas.drawLine(0, xy, maxCoord, xy, boardLinePaint); // horizontal line
            canvas.drawLine(xy, 0, xy, maxCoord, boardLinePaint); // vertical line
        }
    }

    /** Calculate the gap between two horizontal/vertical lines. */
    protected float lineGap() {
        return Math.min(getMeasuredWidth(), getMeasuredHeight()) / (float) boardSize;
    }

    /** Calculate the number of horizontal/vertical lines. */
    private int numOfLines() {
        return boardSize + 1;
    }

    /** Calculate the maximum screen coordinate. */
    protected float maxCoord() {
        return lineGap() * (numOfLines() - 1);
    }

    /**
     * Given screen coordinates, locate the corresponding place in the board
     * and return its coordinates; return -1 if the screen coordinates
     * don't correspond to any place in the board.
     * The returned coordinates are encoded as <code>x*100 + y</code>.
     */
    private int locatePlace(float x, float y) {

        if (x <= maxCoord() && y <= maxCoord()) {
            final float placeSize = lineGap();
            int ix = (int) (x / placeSize);
            int iy = (int) (y / placeSize);
            return ix * 100 + iy;
        }
        return -1;
    }

    /** Register the given listener. */
    public void addBoardTouchListener(BoardTouchListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Unregister the given listener. */
    public void removeBoardTouchListener(BoardTouchListener listener) {
        listeners.remove(listener);
    }

    /** Notify all registered listeners. */
    private void notifyBoardTouch(int x, int y) {
        for (BoardTouchListener listener: listeners) {
            listener.onTouch(x, y);
        }
    }
}
