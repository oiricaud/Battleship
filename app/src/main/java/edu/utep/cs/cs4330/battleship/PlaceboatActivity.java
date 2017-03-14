package edu.utep.cs.cs4330.battleship;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_boat
 */

public class PlaceboatActivity  extends Activity {
    private Board board;
    private BoardView boardView;
    private TextView title;
    private Button next;
    private ImageView aircraftImage;
    private ImageView battleshipImage;
    private ImageView submarineImage;
    private ImageView minesweeperImage;
    private ImageView frigateImage;
    private ViewGroup rootLayout;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_boat);
        // Title and next button
        title = (TextView) findViewById(R.id.placeboats);
        next = (Button) findViewById(R.id.next);

        // Set the board view so boats can be placed on the grid
        board = new Board(10);
        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);

        rootLayout = (ViewGroup) findViewById(R.id.defaultBoatsView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150); // Size of ships


        //Aircraft
        battleshipImage = (ImageView) findViewById(R.id.aircraft);
        battleshipImage.setLayoutParams(layoutParams);
        battleshipImage.setOnTouchListener(new ChoiceToucheListener());

        // Battleship
        aircraftImage = (ImageView) findViewById(R.id.battleship);
        aircraftImage.setLayoutParams(layoutParams);
        aircraftImage.setOnTouchListener(new ChoiceToucheListener());

        // Submarine
        submarineImage = (ImageView) findViewById(R.id.submarine);
        submarineImage.setLayoutParams(layoutParams);
        submarineImage.setOnTouchListener(new ChoiceToucheListener());

        // Minesweeper
        minesweeperImage = (ImageView) findViewById(R.id.minesweeper);
        minesweeperImage.setLayoutParams(layoutParams);
        minesweeperImage.setOnTouchListener(new ChoiceToucheListener());

        // Minesweeper
        frigateImage = (ImageView) findViewById(R.id.frigate);
        frigateImage.setLayoutParams(layoutParams);
        frigateImage.setOnTouchListener(new ChoiceToucheListener());

        // Change font
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/eightbit.TTF");
        title.setTypeface(typeface);
        next.setTypeface(typeface);

        // Go to the next activity
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceboatActivity.this, GameActivity.class);
                PlaceboatActivity.this.startActivity(intent);
                /** Fading Transition Effect */
                PlaceboatActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private final class ChoiceToucheListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // TODO Auto-generated method stub
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }
}
