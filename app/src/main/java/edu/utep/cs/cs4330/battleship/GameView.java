package edu.utep.cs.cs4330.battleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

public class GameView extends Activity {
    private MediaPlayer mp;
    private String fontPath;

    /* Begin Fields for Human */
        private TextView title;
        private Button quit;
        private RelativeLayout rootLayout;
        private BoardView humanBoardView;
        private Ship aircraft = new Ship(5, "aircraft", "Human");
        private Ship battleship = new Ship(4, "battleship", "Human");
        private Ship destroyer = new Ship(3, "destroyer", "Human");
        private Ship submarine = new Ship(3, "submarine", "Human");
        private Ship patrol = new Ship(2, "patrol", "Human");
    /* End Fields for Human */

    /* Begin Fields for AI */
        private BoardView computerBoardView;
        private int countShots = 0;
        private TextView counter;
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

        public String getFontPath() {
            return fontPath;
        }

        public void setFontPath(String fontPath) {
            this.fontPath = fontPath;
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
        setFontPath("fonts/eightbit.TTF");
        // The following loads the corresponding views. This class gets called from @see GameController.
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            String viewToLaunch = extras.getString("viewWeWantToLaunch"); // Look for YOUR KEY, variable you're receiving
            String difficulty = extras.getString("level_of_difficulty"); // Look for YOUR KEY, variable you're receiving
            if(viewToLaunch.equals("launchHomeView")){
                launchHomeView();
            }
            if (viewToLaunch.equals("humanChooseLevelView")) {
                humanChooseLevelView(); // The creation of this activity
            }
            if (viewToLaunch.equals("humanPlaceBoatsView")) {
                humanPlaceBoatsView(difficulty); // The creation of this activity
            }
            if(viewToLaunch.equals("startGameView")){
                startGameView();
            }
        }
    }

    private void humanChooseLevelView() {
        setContentView(R.layout.activity_level);
        Button easy = (Button) findViewById(R.id.easy);
        Button medium = (Button) findViewById(R.id.medium);
        Button hard = (Button) findViewById(R.id.hard);
        TextView chooseLevel = (TextView) findViewById(R.id.chooseDifficulty);

        // Change font to a cooler 8-bit font.
        changeFont(this, easy);
        changeFont(this, medium);
        changeFont(this, hard);
        changeFont(this, chooseLevel);

        // Wait for the user input
        easy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                callTheController("humanPlaceBoatsController", "easy");
            }
        });
        medium.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                callTheController("humanPlaceBoatsController", "medium");
            }
        });
        hard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                callTheController("humanPlaceBoatsController", "hard");
            }
        });
    }



    private void launchHomeView() {
        setContentView(R.layout.home);
        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip); // Change font
        changeFont(this, battleshipLabel);

        // Begin to the next activity, placing boats on the map
        Button startButton = (Button) findViewById(R.id.start);
        changeFont(this, startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTheController("humanChooseLevelController");
            }
        });
    }

    /**
     * This method creates buttons and drag & drop feature the user uses to place boats on grid.
     * @param levelOfDifficultyKey
     */
    private void humanPlaceBoatsView(String levelOfDifficultyKey) {
        setContentView(R.layout.activity_place_ship);
        TextView title = (TextView) findViewById(R.id.placeboats); // PLACE BOATS TITLE
        TextView level_of_difficulty_placeHolder = (TextView) findViewById(R.id.level_of_difficulty_placeHolder);
        level_of_difficulty_placeHolder.setText(levelOfDifficultyKey); // LEVEL OF DIFFICULTY TEXT

        // Set the board view so boats can be placed on the grid
        Board board = new Board(10);
        humanBoardView = (BoardView) findViewById(R.id.boardView);
        humanBoardView.setBoard(board);

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
        changeFont(this, title);
        changeFont(this, quit);
    }

    /**
     * Only allows the user to advanced to the next activity once all boats have been placed
     * on the board grid.
     */
    public void hasHumanPlacedAllBoats() {
        Button next = (Button) findViewById(R.id.next);
        changeFont(this, next);

        // Once the user has place all ships on grid, advance to the next activity
        if (aircraft.isPlaced() && battleship.isPlaced() && destroyer.isPlaced() &&
                submarine.isPlaced() && patrol.isPlaced()) {
            next.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callTheController("startGameController");
                }
            });
        } else { // By default hide the next button and don't let the user advance until all boats have
            // been placed on the grid.
            next.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * @param whatControllerAreWeCalling is the argument we are taking to obtain the corresponding
     *                                   controller @see GameController
     */
    private void callTheController(String whatControllerAreWeCalling) {
        Intent intent = new Intent(GameView.this, edu.utep.cs.cs4330.battleship.GameController.class);
        intent.putExtra("controllerName", whatControllerAreWeCalling);
        GameView.this.startActivity(intent);
        fadingTransition(); // Fading Transition Effect
    }
    private void callTheController(String whatControllerAreWeCalling, String difficulty) {
        Intent intent = new Intent(GameView.this, edu.utep.cs.cs4330.battleship.GameController.class);
        intent.putExtra("controllerName", whatControllerAreWeCalling);
        intent.putExtra("difficulty", difficulty);
        GameView.this.startActivity(intent);
        fadingTransition(); // Fading Transition Effect
    }
    /**
     * Fading Transition Effect
     */
    private void fadingTransition() {
        GameView.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    /**
     *  At random, ships are placed either horizontally or vertically on a 10x10 board.
     *  The user is able to interact with this board and creates (x,y) coordinates.
     *  The user coordinates are compared to the coordinates from all boats that are randomly placed
     *  on the board. If the user hits a boat the method onDraw is invoked from the
     *  @see BoardView.java
     *  and colors a red circle the position of the boats, else colors a white circle indicating the
     *  user missed.
     *
     */
    private void startGameView() {
        setContentView(R.layout.current_game);

        /* Begin Human Board */
            Board humanBoard = new Board(10);
            humanBoardView = (BoardView) findViewById(R.id.humanBoard);
            humanBoardView.setBoard(humanBoard);
        /* End Human Board */

        /* Begin Computer Stuff Game */
            final Context activityContext = this;
            Board computerBoard = new Board(10);
            computerBoardView = (BoardView) findViewById(R.id.computerBoard);
            computerBoardView.setBoard(computerBoard);

            // Below we define the boats that will be placed on the board
            final Ship aircraft = new Ship(5, "aircraft", "Computer");
            final Ship battleship = new Ship(4, "battleship", "Computer");
            final Ship destroyer = new Ship(3, "destroyer", "Computer");
            final Ship submarine = new Ship(3, "submarine", "Computer");
            final Ship patrol = new Ship(2, "patrol", "Computer");

            // Define buttons and text views here
            TextView battleshipTitle = (TextView) findViewById(R.id.BattleShip);
            counter = (TextView) findViewById(R.id.countOfHits);
            Button newButton = (Button) findViewById(R.id.newButton);
            Button quitButton = (Button) findViewById(R.id.quitButton);

            // Change font
            changeFont(this, newButton);
            changeFont(this, quitButton);
            changeFont(this, battleshipTitle);
            changeFont(this, counter);

            // The predefined methods that allow the user to quit or start a new game
            newActivity(newButton, activityContext);
            quitActivity(quitButton, activityContext);

            // The counter displays the number of shots in the UI, the user has tapped on the board.
            countShots = 0;
            setCountShots(0);
        /* End Computer Stuff Game*/

        // Call the GameController to see whose turn it is.
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
                    makeExplosionSound(activityContext);
                    aircraft.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (aircraft.getHit() == 5) {
                        toast("Aircraft SUNK");
                        makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(battleship.getComputerCordinates(), x, y)) {
                    makeExplosionSound(activityContext);
                    battleship.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (battleship.getHit() == 4) {
                        toast("Battleship SUNK");
                        makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(destroyer.getComputerCordinates(), x, y)) {
                    makeExplosionSound(activityContext);
                    destroyer.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (destroyer.getHit() == 3) {
                        toast("Destroyer SUNK");
                        makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(submarine.getComputerCordinates(), x, y)) {
                    makeExplosionSound(activityContext);
                    submarine.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (submarine.getHit() == 3) {
                        toast("Submarine SUNK");
                        makeLouderExplosion(activityContext);
                    }
                } else if (isItAHit(patrol.getComputerCordinates(), x, y)) {
                    makeExplosionSound(activityContext);
                    patrol.hit();
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    if (patrol.getHit() == 2) {
                        toast("Patrol SUNK");
                        makeLouderExplosion(activityContext);
                    }
                } else {
                    computerBoardView.setxMiss(x);
                    computerBoardView.setyMiss(y);
                    toast("That was close!");
                    makeMissedSound(activityContext);
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
                                callTheController("quitController");
                                fadingTransition(); // Fading Transition Effect
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
                                callTheController("humanChooseLevelController");
                                fadingTransition(); // Fading Transition Effect
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

    /** Plays at random 3 default game songs.
     * @param context is the activity context
     */
    /**
     * Makes a swish noise when the player misses a shot.
     * @param context is the activity context
     */
    public void makeMissedSound(Context context) {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.missed);
        mp.start();
    }
    /**
     * Makes an explosion sound if the user hits a boat.
     * @param context is the activity context
     */
    public void makeExplosionSound(Context context) {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.torpedo);
        mp.start();
    }
    /**
     * Makes a louder explosion as the latter method.
     * @param context is the activity context
     */
    public void makeLouderExplosion(Context context) {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.sunk);
        mp.start();
    }

    public void changeFont(Context context, TextView textView){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), getFontPath());
        textView.setTypeface(typeface);
    }
}

