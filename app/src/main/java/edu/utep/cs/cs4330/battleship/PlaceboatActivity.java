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

public class PlaceboatActivity  extends Activity implements View.OnTouchListener, View.OnDragListener {
    private Board board;
    private BoardView boardView;
    private TextView title;
    private Button next;

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

        // Drag ships
        findViewById(R.id.aircraft).setOnTouchListener(this);
        findViewById(R.id.battleship).setOnTouchListener(this);
        findViewById(R.id.minesweeper).setOnTouchListener(this);
        findViewById(R.id.submarine).setOnTouchListener(this);
        findViewById(R.id.frigate).setOnTouchListener(this);

        // Place ships to this view only
        findViewById(R.id.defaultBoatsView).setOnDragListener(this);
        findViewById(R.id.gridBoatsView).setOnDragListener(this);

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
    @Override
    public boolean onDrag(View v, DragEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == DragEvent.ACTION_DROP) {
            //we want to make sure it is dropped only to left and right parent view
            View view = (View) event.getLocalState();

            if (v.getId() == R.id.gridBoatsView || v.getId() == R.id.defaultBoatsView) {
                boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
                    @Override
                    public void onTouch(int x, int y) {

                    }
                    });
                ViewGroup source = (ViewGroup) view.getParent();
                source.removeView(view);
                RelativeLayout target = (RelativeLayout) v;
                target.addView(view);
            }
            //make view visible as we set visibility to invisible while starting drag
            view.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(null, shadowBuilder, view, 0);
            } else {
                view.startDrag(null, shadowBuilder, view, 0);
            }
            view.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }
}
