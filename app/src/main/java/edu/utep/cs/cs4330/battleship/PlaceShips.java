package edu.utep.cs.cs4330.battleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_ship
 */

public class PlaceShips extends Activity {

    /* Begin Fields for Human */
    private TextView title;
    private Button quit;
    private RelativeLayout rootLayout;
    private Font eightBitFont = new Font("fonts/eightbit.TTF");
    private BoardView humanBoardView;
    private Ship aircraft = new Ship(5, "aircraft", "Human");
    private Ship battleship = new Ship(4, "battleship", "Human");
    private Ship destroyer = new Ship(3, "destroyer", "Human");
    private Ship submarine = new Ship(3, "submarine", "Human");
    private Ship patrol = new Ship(2, "patrol", "Human");
    private LinkedList<Integer> coordinatesForAllBoatsHuman = new LinkedList<Integer>();
    /* End Fields for Human */

    /* Begin Fields for AI */
    private BoardView computerBoardView;
    private int countShots = 0;
    private TextView counter;
    private Music shipSound = new Music();
    private LinkedList<Integer> coordinatesForAllBoatsAI = new LinkedList<Integer>();
    /* End Fields for AI */

    /* Begin Setters and Getters */
        /**
         * @return the number of shots the user has shot at boats
         */
    public int getCountShots() {
        return countShots;
    }
        /**
         * @param countShots set the count of shots each time the user fires.
         */
    public void setCountShots(int countShots) {
        this.countShots = countShots;
    }

    public LinkedList<Integer> getCoordinatesForAllBoatsAI() {
        return coordinatesForAllBoatsAI;
    }

    public void setCoordinatesForAllBoatsAI(LinkedList<Integer> coordinatesForAllBoatsAI) {
        this.coordinatesForAllBoatsAI = coordinatesForAllBoatsAI;
    }

    public LinkedList<Integer> getCoordinatesForAllBoatsHuman() {
        return coordinatesForAllBoatsHuman;
    }

    public void setCoordinatesForAllBoatsHuman(LinkedList<Integer> coordinatesForAllBoatsHuman) {
        this.coordinatesForAllBoatsHuman = coordinatesForAllBoatsHuman;
    }

    /* End Setters and Getters */

    /**
     * @param savedInstanceState This class gets called from @see GameController
     *                           also receiving level_of_difficulty from the human.
     *                           It then sets everything for the human and computer.
     *                           For example the computer based on level of difficulty must place
     *                           boats at random. The human in the other hand must manually place
     *                           the objects (boat images) on the board.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_ship);

        if (getIntent().getExtras() != null) {
            TextView level_of_difficulty_placeHolder = (TextView) findViewById(R.id.level_of_difficulty_placeHolder);
            Bundle extras = getIntent().getExtras();
            String levelOfDifficultyKey = extras.getString("level_of_difficulty"); // Look for YOUR KEY, variable you're receiving
            String typeOfUserKey = extras.getString("userType");
            String startGame = extras.getString("shouldWeStartGame");

            if (typeOfUserKey.equals("human")) {
                level_of_difficulty_placeHolder.setText(levelOfDifficultyKey);
                setEverythingForHuman(); // The creation of this activity
            }
            else if (typeOfUserKey.equals("computer")) {
                Log.w(" Here", "1Here");
                setEverythingForComputer();
            }
            if(startGame.equals("true")){
                beginGame(getCoordinatesForAllBoatsAI(), getCoordinatesForAllBoatsHuman());
            }
        }
    }

    private void setEverythingForComputer() {
       // setContentView(R.layout.activity_game);
       // computerBoardView = (BoardView) findViewById(R.id.boardView);
       // computerBoardView.setBoard(computerBoard);

//        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip);
//        eightBitFont.changeFont(this, battleshipLabel); // Change font


        countShots = 0;
        setCountShots(0);



        final Context context = this; // Needed for the alert dialogue builder

        // s3 now points to s2, s3->s2, where the user is able to place new ships.
        // Start a new game when clicked button
     //   Button newButton = (Button) findViewById(R.id.newButton);
     //   newActivity(newButton, this);

        // s3 -> s0
        // Takes the user back to the starting state
    //    Button quitButton = (Button) findViewById(R.id.quitButton);
     //   quitActivity(quitButton, context);
    //    eightBitFont.changeFont(this, newButton);
     //   eightBitFont.changeFont(this, quitButton);

        Intent intent = new Intent(PlaceShips.this, edu.utep.cs.cs4330.battleship.GameController.class);
        intent.putExtra("methodName", "startGameView");
        PlaceShips.this.startActivity(intent);
        /** Fading Transition Effect */
        PlaceShips.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


       // beginGame(getCoordinatesForAllBoatsAI(), getCoordinatesForAllBoatsHuman());
    }
    /**
     * This method creates buttons and drag & drop feature the user uses to place boats on grid.
     */
    private void setEverythingForHuman() {
        title = (TextView) findViewById(R.id.placeboats); // PLACE BOATS

        // SET BOARD
        // Set the board view so boats can be placed on the grid
        Board board = new Board(10);
        humanBoardView = (BoardView) findViewById(R.id.boardView);
        humanBoardView.setBoard(board);

        // SET BUTTONS
        hasHumanPlacedAllBoats(); // Hide the "NEXT" button by default
        quit = (Button) findViewById(R.id.quitB);
        quitActivity(quit, this);
        // Displays all 5 boats on UI & sets up the images to be able to drag and drop method
        // that is later used.
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
    public void hasHumanPlacedAllBoats() {
        Button next = (Button) findViewById(R.id.next);
        eightBitFont.changeFont(this, next);

        // Once the user has place all ships on grid, advance to the next activity
        if (aircraft.isPlaced() && battleship.isPlaced() && destroyer.isPlaced() &&
                submarine.isPlaced() && patrol.isPlaced()) {
            next.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PlaceShips.this, edu.utep.cs.cs4330.battleship.GameController.class);
                    intent.putExtra("methodName", "computerPlaceBoatsView");
                    PlaceShips.this.startActivity(intent);
                    /** Fading Transition Effect */
                    PlaceShips.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        } else { // By default hide the next button and don't let the user advance until all boats have
            // been placed on the grid.
            next.setVisibility(View.INVISIBLE);
        }
    }

    private void beginGame(LinkedList<Integer> coordinatesForAllBoatsAI, LinkedList<Integer> coordinatesForAllBoatsHuman) {
        setContentView(R.layout.current_game);
        Log.w("coordinates4AllBoatsAI", coordinatesForAllBoatsAI.toString());

        /* Begin Human Board */
        Board humanBoard = new Board(10);
        BoardView humanBoardView = (BoardView) findViewById(R.id.humanBoard);
        humanBoardView.setBoard(humanBoard);
        /* End Human Board */

        /* Begin Computer Game */
        Board computerBoard = new Board(10);
        computerBoardView = (BoardView) findViewById(R.id.computerBoard);
        computerBoardView.setBoard(computerBoard);
        // Below we define the boats that will be placed on the board
        final Ship aircraft = new Ship(5, "aircraft", "Computer");
        final Ship battleship = new Ship(4, "battleship", "Computer");
        final Ship destroyer = new Ship(3, "destroyer", "Computer");
        final Ship submarine = new Ship(3, "submarine", "Computer");
        final Ship patrol = new Ship(2, "patrol", "Computer");
        /* End Computer Board Game*/
        countShots = 0;
        setCountShots(0);
        final Context activityContext = this;
        // The counter displays the number of shots in the UI, the user has tapped on the board.
        counter = (TextView) findViewById(R.id.countOfHits);
        eightBitFont.changeFont(this, counter); // Change font

        // Listen for the user input
        computerBoardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                setCountShots(countShots + 1);
                counter.setText(String.valueOf("Number of Shots: " + getCountShots()));

                // Compare the coordinates the user just touched with any of the boats that are placed
                // on the board. Then either play a missed or explosion sound. When the boat sinks
                // play a louder explosion.
                if (isItAHit(aircraft.getComputerCordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    aircraft.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (aircraft.getHit() == 5) {
                        toast("Aircraft SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(battleship.getComputerCordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    battleship.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (battleship.getHit() == 4) {
                        toast("Battleship SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(destroyer.getComputerCordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    destroyer.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (destroyer.getHit() == 3) {
                        toast("Destroyer SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(submarine.getComputerCordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    submarine.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (submarine.getHit() == 3) {
                        toast("Submarine SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(patrol.getComputerCordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    patrol.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (patrol.getHit() == 2) {
                        toast("Patrol SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                } else {
                    computerBoardView.setxMiss(x);
                    computerBoardView.setyMiss(y);
                    toast("That was close!");
                    shipSound.makeMissedSound(activityContext);
                }
            }
        });
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
                    humanBoardView.locatePlace(X, Y);
                    int height = humanBoardView.getMeasuredHeight();
                    int width = humanBoardView.getMeasuredWidth();
                    if ((X <= 1000 && Y <= 1050)) {
                        layoutParams.leftMargin = X;
                        layoutParams.topMargin = Y;
                        Log.w("Get tag", String.valueOf(view.getTag()));

                        if (view.getTag() == "aircraft") {
                            Log.w("aircraft", "aircraft");
                            aircraft.setPlaced(true);
                        } else if (view.getTag() == "battleship") {
                            Log.w("battleship", "battleship");
                            battleship.setPlaced(true);
                        } else if (view.getTag() == "destroyer") {
                            Log.w("destroyer", "destroyer");
                            destroyer.setPlaced(true);
                        } else if (view.getTag() == "submarine") {
                            Log.w("submarine", "submarine");
                            submarine.setPlaced(true);
                        } else if (view.getTag() == "patrol") {
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
            hasHumanPlacedAllBoats();
            rootLayout.invalidate();
            return true;
        }
    }

    /**
     * @param coordinates are the coordinates from the user.
     * @param x           is the number of rows - 1.
     * @param y           is the number of columns - 1.
     * @return If the user hits a boat return true else false.
     */
    private boolean isItAHit(int[][] coordinates, int x, int y) {
        return coordinates[x][y] == 1;
    }

    /**
     * Show a toast message.
     */
    protected void toast(String msg) {
        final Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }

    /**
     * Restarts the activity
     */
    public void restartActivity(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    public void quitActivity(Button quitButton, final Context context){
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Alert Dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to quit?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                toast("Quiting Game!");
                                Intent intent = new Intent(PlaceShips.this, GameController.class);
                                PlaceShips.this.startActivity(intent);
                                /** Fading Transition Effect */
                                PlaceShips.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    private void newActivity(Button newButton, final Context context) {
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Alert Dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to start a new Game?");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                toast("New Game successfully created!");
                                restartActivity();
                                Intent intent = new Intent(PlaceShips.this, PlaceShips.class);
                                PlaceShips.this.startActivity(intent);
                                PlaceShips.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}

