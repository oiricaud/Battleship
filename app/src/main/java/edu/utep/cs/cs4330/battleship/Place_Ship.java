package edu.utep.cs.cs4330.battleship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_ship
 */

public class Place_Ship extends Activity {
    private TextView title;
    private Button quit;
    private RelativeLayout rootLayout;
    private Font eightBitFont = new Font("fonts/eightbit.TTF");
    private BoardView boardView;
    private Ship aircraft = new Ship (5, "aircraft", "Human");
    private Ship battleship = new Ship(4, "battleship", "Human");
    private Ship destroyer = new Ship(3, "destroyer", "Human");
    private Ship submarine = new Ship(3, "submarine", "Human");
    private Ship patrol = new Ship(2, "patrol", "Human");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_ship);
        setEverything(); // The creation of this activity

        // Receiving level_of_difficulty from @see GameController
        if( getIntent().getExtras() != null) {
            TextView level_of_difficulty_placeHolder = (TextView) findViewById(R.id.level_of_difficulty_placeHolder);
            Bundle extras = getIntent().getExtras();
            String levelOfDifficulty = extras.getString("level_of_difficulty"); // Look for YOUR KEY, variable you're receiving
            level_of_difficulty_placeHolder.setText(levelOfDifficulty);
        }
    }
    /**
     * This method creates buttons and drag & drop feature the user uses to place boats on grid.
     */
    private void setEverything() {
        title = (TextView) findViewById(R.id.placeboats); // PLACE BOATS
        setBoard();
        setButtons();           // Sets the appropriate buttons
        setBoatImagesOnView(); // Displays all 5 boats on UI
    }

    private void setBoard() {
        // Set the board view so boats can be placed on the grid
        Board board = new Board(10);
        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);
    }
    /**
     *  The user needs to be able to traverse to next or quit, hence the maker creates buttons.
     */
        private void setButtons() {
        haveAllBoatsBeenPlaced(); // Hide the "NEXT" button by default
        quit =(Button) findViewById(R.id.quitB);

        // Quit and go to the Launch View aka Home.
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Place_Ship.this, GameController.class);
                Place_Ship.this.startActivity(intent);
                /** Fading Transition Effect */
                Place_Ship.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
    /**
     * This method sets up the images to be able to drag and drop method that is later used.
     */
    private void setBoatImagesOnView() {
        rootLayout = (RelativeLayout) findViewById(R.id.defaultBoatsView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100); // Size of ships

        //Aircraft
        ImageView aircraftImage = (ImageView) findViewById(R.id.aircraft);
        aircraftImage.setTag("aircraft");
        aircraftImage.setLayoutParams(layoutParams);
        aircraftImage.setOnTouchListener(new ChoiceToucheListener());

        // Battleship
        ImageView battleshipImage = (ImageView) findViewById(R.id.battleship);
        battleshipImage.setTag("battleship");
        battleshipImage.setLayoutParams(layoutParams);
        battleshipImage.setOnTouchListener(new ChoiceToucheListener());

        // Destroyer
        ImageView destroyerImage = (ImageView) findViewById(R.id.destroyer);
        destroyerImage.setTag("destroyer");
        destroyerImage.setLayoutParams(layoutParams);
        destroyerImage.setOnTouchListener(new ChoiceToucheListener());

        // Submarine
        ImageView submarineImage = (ImageView) findViewById(R.id.submarine);
        submarineImage.setTag("submarine");
        submarineImage.setLayoutParams(layoutParams);
        submarineImage.setOnTouchListener(new ChoiceToucheListener());

        // Patrol
        ImageView patrolImage = (ImageView) findViewById(R.id.patrol);
        patrolImage.setTag("patrol");
        patrolImage.setLayoutParams(layoutParams);
        patrolImage.setOnTouchListener(new ChoiceToucheListener());

        // Change font
        eightBitFont.changeFont(this, title);
        eightBitFont.changeFont(this, quit);
    }
    /**
     * Only allows the user to advanced to the next activity once all boats have been placed
     * on the board grid.
     */
    public void haveAllBoatsBeenPlaced(){
        Button next = (Button) findViewById(R.id.next);
        eightBitFont.changeFont(this, next);

        // Once the user has place all ships on grid, advance to the next activity
        if(aircraft.isPlaced() && battleship.isPlaced() && destroyer.isPlaced() &&
                submarine.isPlaced() && patrol.isPlaced()){
            next.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Place_Ship.this, edu.utep.cs.cs4330.battleship.GameController.class);
                    intent.putExtra("methodName", "computerPlaceBoatsView");
                    Place_Ship.this.startActivity(intent);
                    /** Fading Transition Effect */
                    Place_Ship.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }
        else{ // By default hide the next button and don't let the user advance until all boats have
            // been placed on the grid.
            next.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * The drag and drop feature
     */
    private final class ChoiceToucheListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // TODO Auto-generated method stub
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    int _xDelta = X;
                    int _yDelta = Y;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    boardView.locatePlace(X, Y);
                    int height = boardView.getMeasuredHeight();
                    int width = boardView.getMeasuredWidth();
                    if((X <= 1000 && Y <= 1050)){
                        layoutParams.leftMargin = X;
                        layoutParams.topMargin = Y;
                        Log.w("Get tag", String.valueOf(view.getTag()));

                        if(view.getTag() == "aircraft"){
                            Log.w("aircraft", "aircraft");
                            aircraft.setPlaced(true);
                        }
                        else if(view.getTag() == "battleship"){
                            Log.w("battleship", "battleship");
                            battleship.setPlaced(true);
                        }
                        else if(view.getTag() == "destroyer"){
                            Log.w("destroyer", "destroyer");
                            destroyer.setPlaced(true);
                        }
                        else if(view.getTag() == "submarine"){
                            Log.w("submarine", "submarine");
                            submarine.setPlaced(true);
                        }
                        else if(view.getTag() == "patrol"){
                            Log.w("patrol", "patrol");
                            patrol.setPlaced(true);
                        }
                        Log.w("height", String.valueOf(height));
                        Log.w("width", String.valueOf(width));
                        Log.w("X", String.valueOf(X));
                        Log.w("Y", String.valueOf(Y));
                        view.setLayoutParams(layoutParams);
                        break;
                    }
            }
            haveAllBoatsBeenPlaced();
            rootLayout.invalidate();
            return true;
        }
    }
}

