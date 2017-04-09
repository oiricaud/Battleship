package edu.utep.cs.cs4330.battleship;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_ship
 */

@SuppressWarnings("ALL")
public class GameController extends Activity {
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static MediaPlayer mp;                     // For boats sound effects
    private static MediaPlayer music;                  // For music background
    private RetainedFragment mRetainedFragment; // If the screen is changed we can restore data and layouts
    private String fontPath;
    private GameModel gameModel = new GameModel();

    /* BEGIN GETTERS AND SETTERS */
    private String getFontPath() {
        return fontPath;
    }

    private void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    /* END GETTERS AND SETTERS */

    /**
     * @param savedInstanceState The creation of this mobile application. It calls a method that displays the
     *                           an aesthetic background image with the title of Battleship in 8-bit style.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFontPath("fonts/brandonlight.TTF");

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        mRetainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new RetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
            mRetainedFragment.setData(gameModel);
        }
        launchHomeView();   // By default, this activity will always display until an event occurs.
    }

    @Override
    public void onStart() {

        try {
            if (music != null) {
                music.stop();
                music.release();
                music = null;
            }
            music = MediaPlayer.create(this, R.raw.stratus);
            if (gameModel.getMusicTimer() > 0) {
                music.seekTo(gameModel.getMusicTimer());
            }
            music.setLooping(true);
            music.start();
        } catch (Exception e) {
            Log.w("Wut", "Check onStart(), there might be an issue");
        }
        super.onStart();
    }


    @Override
    protected void onPause() {
        if (gameModel.isMediaPlaying(music) == true) {
            music.pause();
            gameModel.setMusicTimer(music.getCurrentPosition());
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (music != null && !music.isPlaying())
            music.start();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (music != null) {
            music.stop();
            music.release();
            music = null;
        }
        super.onDestroy();
    }

    /* BEGIN SCREEN CONFIGURATIONS LOGIC */
    /**
     * @param newConfig When the user rotates their phone either from portrait to landscape or landscape to portrait,
     *                  often times activities are destroyed. This method stores the current view the user was currently
     *                  on and if applicable stores the other data i.e coordinates boats, etc.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Gets the state the user was on before the screen was oriented
        gameModel = mRetainedFragment.getCurrentGameState();

        // Checks the orientation of the screen
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                // Launch the views
                //noinspection Duplicates
                switch (mRetainedFragment.getCurrentView()) {
                    case "launchHomeView":
                        launchHomeView();
                        break;
                    case "chooseLevelView":
                        chooseLevelView();
                        break;
                    case "placeBoatsView":
                        placeBoatsView();
                        break;
                    case "playGameView":
                        playGameView(gameModel.getHumanPlayer().boardView);
                        break;
                }
                //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                gameModel = mRetainedFragment.getCurrentGameState();
                //noinspection Duplicates
                switch (mRetainedFragment.getCurrentView()) {
                    case "launchHomeView":
                        launchHomeView();
                        break;
                    case "chooseLevelView":
                        chooseLevelView();
                        break;
                    case "placeBoatsView":
                        placeBoatsView();
                        break;
                    case "playGameView":
                        playGameView(gameModel.getHumanPlayer().boardView);
                        break;
                }
                //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /* END SCREEN CONFIGURATIONS LOGIC */

/* BEGIN VIEWS */
    /**
     * This method launches the home.xml and waits for the user to begin a new game.
     */
    private void launchHomeView() {
        mRetainedFragment.setCurrentView("launchHomeView");
        setContentView(R.layout.home);
        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip); // Change font
        changeFont(battleshipLabel);

        final Button oneVersusOneButton = (Button) findViewById(R.id.OneVersusOne);
        final Button onlineButton = (Button) findViewById(R.id.Computer);
        oneVersusOneButton.setVisibility(View.INVISIBLE);
        onlineButton.setVisibility(View.INVISIBLE);

        // Begin to the next activity, placing boats on the map
        final Button startButton = (Button) findViewById(R.id.start);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneVersusOneButton.setVisibility(View.VISIBLE);
                onlineButton.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);


                oneVersusOneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Set up network
                        gameModel.setTypeOfGame("1 VS 1");

                        toast("This feauture will come soon!");
                    }
                });
                onlineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameModel.setTypeOfGame("1 VS PC");
                        placeBoatsView();
                    }
                });
            }
        });
    }

    /**
     * This method launches the activity_level.xml and waits for the user to enter a
     * difficulty level.
     */
    private void chooseLevelView() {
        setContentView(R.layout.activity_level);
        mRetainedFragment.setCurrentView("chooseLevelView");
        Button easy = (Button) findViewById(R.id.easy);
        Button medium = (Button) findViewById(R.id.medium);
        Button hard = (Button) findViewById(R.id.hard);
        TextView chooseLevel = (TextView) findViewById(R.id.chooseDifficulty);

        // Change font to a cooler 8-bit font.
        changeFont(easy);
        changeFont(medium);
        changeFont(hard);
        changeFont(chooseLevel);

        // Wait for the user input
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameModel.setDifficulty("easy");
                placeBoatsView();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameModel.setDifficulty("medium");
                placeBoatsView();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameModel.setDifficulty("hard");
                placeBoatsView();
            }
        });
    }

    /**
     * This method sets the toolbar for the user to tap on the board to place the boats of the ships
     */
    private void placeBoatsView() {
        setContentView(R.layout.activity_human_place_boats);
        mRetainedFragment.setCurrentView("placeBoatsView");

        /* Define the image objects */
        ImageView aircraft = (ImageView) findViewById(R.id.aircraft);
        ImageView battleship = (ImageView) findViewById(R.id.battleship);
        ImageView destroyer = (ImageView) findViewById(R.id.destroyer);
        ImageView submarine = (ImageView) findViewById(R.id.submarine);
        ImageView patrol = (ImageView) findViewById(R.id.patrol);
        gameModel.getHumanPlayer().boardView = (BoardView) findViewById(R.id.humanBoardView);
        gameModel.getHumanPlayer().boardView.setBoard(gameModel.getHumanPlayer().gameBoard);

        // This means the user had already placed boats on the grid but decided to go back to this view and perhaps
        // change the boats.
        if (gameModel.getHumanPlayer().gameBoard.grid != null) {
            gameModel.getHumanPlayer().boardView.coordinatesOfHumanShips = gameModel.getHumanPlayer().gameBoard.grid;
            gameModel.getHumanPlayer().boardView.invalidate();
        }

        /* Allow these boat images to be draggable, listen when they are touched */
        aircraft.setOnTouchListener(new MyTouchListener());
        battleship.setOnTouchListener(new MyTouchListener());
        destroyer.setOnTouchListener(new MyTouchListener());
        submarine.setOnTouchListener(new MyTouchListener());
        patrol.setOnTouchListener(new MyTouchListener());

        /* Define the particular location where these boat images are allowed to be dragged onto */
        findViewById(R.id.humanBoardPlacer).setOnDragListener(new MyDragListener());

        Button next = (Button) findViewById(R.id.next);
        Button random = (Button) findViewById(R.id.random);
        changeFont(next);
        changeFont(random);
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playGameView(gameModel.getHumanPlayer().boardView);
            }
        });
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chooseLevelView();
            }
        });
    }

    /**
     * At random, ships are placed either horizontally or vertically on a 10x10 board.
     * The user is able to interact with this board and creates (x,y) coordinates.
     * The user coordinates are compared to the coordinates from all boats that are randomly placed
     * on the board. If the user hits a boat the method onDraw is invoked from the
     *
     * @see BoardView.java
     * and colors a red circle the position of the boats, else colors a white circle indicating the
     * user missed.
     */
    private void playGameView(BoardView copyOfHumanBoard) {
        setContentView(R.layout.current_game);
        mRetainedFragment.setCurrentView("playGameView");
        /* Define Human Board */
        gameModel.getHumanPlayer().boardView = (BoardView) findViewById(R.id.humanBoard);
        gameModel.getHumanPlayer().boardView.setBoard(gameModel.getHumanPlayer().gameBoard);

        // Get the coordinates from the previous activity to this activity
        gameModel.getHumanPlayer().boardView.coordinatesOfHumanShips = copyOfHumanBoard.coordinatesOfHumanShips;
        /* End Human Board */

        /* Begin Computer Stuff Game */
        final Context activityContext = this;

        gameModel.getComputerPlayer().boardView = (BoardView) findViewById(R.id.computerBoard);
        gameModel.getComputerPlayer().boardView.setBoard(gameModel.getComputerPlayer().gameBoard);

        // Define buttons and text views here
        TextView battleshipTitle = (TextView) findViewById(R.id.BattleShip);
        final TextView counter = (TextView) findViewById(R.id.countOfHits);
        Button newButton = (Button) findViewById(R.id.newButton);
        Button quitButton = (Button) findViewById(R.id.quitButton);

        // Change font
        changeFont(newButton);
        changeFont(quitButton);
        changeFont(battleshipTitle);
        changeFont(counter);

        // The predefined methods that allow the user to quit or start a new game
        newActivity(newButton, activityContext);
        quitActivity(quitButton, activityContext);

        /* End Computer Stuff Game*/
        Log.w("Computers board", Arrays.deepToString(gameModel.getComputerPlayer().gameBoard.grid));
        Log.w("Humans board", Arrays.deepToString(gameModel.getHumanPlayer().gameBoard.grid));
        gameModel.getComputerPlayer().boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            /* After player taps on computers board */
            @Override
            public void onTouch(int x, int y) {

                // Human shoots at Computers board
                if (gameModel.getHumanPlayer().shootsAt(gameModel.getComputerPlayer().gameBoard, x, y)) { // Human hits a boat, paint red
                    makeExplosionSound(activityContext);
                    toast("HIT");
                    gameModel.getComputerPlayer().boardView.gameCoordinates[x][y] = 8; // Set it to 8 to indicate it is a hit
                } else { // Human misses, paint computers board white
                    gameModel.getComputerPlayer().boardView.gameCoordinates[x][y] = -9; // Set it to -9 to indicate it is a miss
                    toast("That was close!");
                    makeMissedSound(activityContext);
                    gameModel.getHumanPlayer().shoots(); // Increment counter for # of shots
                    counter.setText(String.valueOf("Number of Shots: " + gameModel.getHumanPlayer().getNumberOfShots()));

                    // COMPUTER SHOOT AT HUMAN BOARD
                    int randomX = generateRandomCoordinate(); // Generate random coordinates
                    int randomY = generateRandomCoordinate(); // Generate random coordinates

                    if (gameModel.getComputerPlayer().shootsAt(gameModel.getHumanPlayer().gameBoard, randomX, randomY)) {
                        makeExplosionSound(activityContext);
                        toast("HIT");
                        gameModel.getHumanPlayer().boardView.gameCoordinates[randomX][randomY] = 8; // Set it to 8 to indicate it is a hit
                        gameModel.getHumanPlayer().boardView.invalidate();
                    } else {
                        gameModel.getHumanPlayer().boardView.gameCoordinates[randomX][randomY] = -9; // Set it to -9 to indicate it is a miss
                        toast("That was close!");
                        makeMissedSound(activityContext);
                    }
                    gameModel.getHumanPlayer().boardView.invalidate();
                }
                mRetainedFragment.setData(gameModel);
            }
        });
    }
/* END VIEWS */

    private int generateRandomCoordinate() {
        Random random = new Random();
        return random.nextInt(10);
    }

/* BEGIN ACTIVITIY EFFECTS */

    /**
     * Fading Transition Effect
     */
    private void fadingTransition() {
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Show a toast message.
     */
    private void toast(String msg) {
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
/* END ACTIVITIY EFFECTS */

    /**
     * Restarts the activity
     */
    public void restartActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

/* BEGIN BUTTONS LOGIC */

    /**
     * Using a stack to keep track the view the user is in.
     */
    @Override
    public void onBackPressed() {
        if (!mRetainedFragment.getCurrentView().isEmpty()) {
            String viewToLaunch = mRetainedFragment.getCurrentView();
            Log.w("temp", viewToLaunch);
            switch (viewToLaunch) {
                case "chooseLevelView":
                    launchHomeView();
                    break;
                case "placeBoatsView":
                    chooseLevelView();
                    break;
                case "playGameView":
                    placeBoatsView();
                    break;
                default:
                    launchHomeView();
            }
        }
    }

    /**
     * @param quitButton Where is the type of button
     * @param context    when the user hits quit, the user is sent back to the first activity
     */
    private void quitActivity(final Button quitButton, final Context context) {
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
                                launchHomeView();
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
                                chooseLevelView();
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
/* END BUTTONS LOGIC */

    /* BEGIN BATTLESHIP MUSIC */
    private void playMusic(Context context) {
        if (music != null) {
            music.stop();
            music.reset();
            music.release();
        }
        // Play one of these random songs in the background.
        Random random = new Random();
        int obtainRandomNumber = random.nextInt(3);
        if (obtainRandomNumber == 1) {
            music = MediaPlayer.create(context, R.raw.alterego);
        }
        if (obtainRandomNumber == 2) {
            music = MediaPlayer.create(context, R.raw.oblivion);
        }
        if (obtainRandomNumber == 3) {
            music = MediaPlayer.create(context, R.raw.yolo);
        }
        music.start();

    }

    private void pauseMusic(Context context) {
        if (music.isPlaying()) {
            music.pause();
        }
    }
    /**
     * Plays at random 3 default game songs. Makes a swish noise when the player misses a shot.
     *
     * @param context is the activity context
     */
    private void makeMissedSound(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.missed);
        mp.start();
    }

    /**
     * Makes an explosion sound if the user hits a boat.
     *
     * @param context is the activity context
     */
    private void makeExplosionSound(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.torpedo);
        mp.start();
    }

    /**
     * Makes a louder explosion as the latter method.
     *
     * @param context is the activity context
     */
    public void makeLouderExplosion(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.sunk);
        mp.start();
    }
/* END BATTLESHIP MUSIC */

    /**
     * Changes the font of the activity
     *
     * @param context  Is the
     * @param textView the view we want to change font to
     */
    private void changeFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), getFontPath());
        textView.setTypeface(typeface);
    }

    @SuppressWarnings("deprecation")
    private class MyDragListener implements View.OnDragListener {
        @SuppressWarnings("deprecation")
        Drawable accept = getResources().getDrawable(R.drawable.accept);
        @SuppressWarnings("deprecation")
        Drawable reject = getResources().getDrawable(R.drawable.reject);
        @SuppressWarnings("deprecation")
        Drawable neutral = getResources().getDrawable(R.drawable.neutral);
        @SuppressWarnings("deprecation")
        Drawable board_color = getResources().getDrawable(R.drawable.board_color);
        // Make images smaller
        int width = 100;
        int height = 100;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
        private boolean getResult = false; // Determine if the an image object has been dragged
        private int tempX = 0;
        private int tempY = 0;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int sdk = android.os.Build.VERSION.SDK_INT;
            v.setBackground(board_color);
            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:
                    //noinspection deprecation
                    v.setBackgroundDrawable(neutral);
                    v.setBackground(board_color);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        //noinspection deprecation
                        v.setBackgroundDrawable(neutral);
                        v.setBackground(board_color);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //noinspection deprecation
                            v.setBackgroundDrawable(neutral);
                            v.setBackground(board_color);
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        //noinspection deprecation
                        v.setBackground(board_color);
                        //   v.setBackgroundDrawable(normalShape);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //  v.setBackground(normalShape);
                            v.setBackground(board_color);
                        }
                    }

                    break;
                case DragEvent.ACTION_DROP:

                    //noinspection deprecation
                    v.setBackgroundDrawable(accept);
                    getResult = true;
                    int[][] boatsCoordinates = new int[10][10];

                    View view = (View) event.getLocalState(); // Current image
                    ViewGroup owner = (ViewGroup) view.getParent(); // RelativeLayout id: humanBoardPlacer
                    view.setLayoutParams(parms);
                    owner.removeView(view);                         // Remove the current image from the humanBoardPlacer
                    RelativeLayout container = (RelativeLayout) v; // Container for boardView

                    String boatWeAreDragging = getResources().getResourceEntryName(view.getId());
                    Log.w("view get height", String.valueOf(view.getHeight()) + " view get Width" + String.valueOf(view.getWidth()));
                    Log.w("owner x", String.valueOf(owner.getX()) + " owner y " + String.valueOf(owner.getY()));
                    Log.w("event.getX()", String.valueOf(event.getX()) + "event.getY()" + String.valueOf(event.getY()));
                    // Round to the nearest 50
                    int convertX = (int) ((event.getX() + 49) / 50) * 50;
                    int convertY = (int) ((event.getY() + 49) / 50) * 50;
                    if (convertX < 525 && convertY >= 50 && convertY <= 850) {
                        // Place boat at the dragged coordinate
                        Log.w("round X", String.valueOf(convertX) + "round Y" + String.valueOf(convertY));
                        view.setX(convertX - 50);
                        view.setY(convertY - 50);

                        // Store the coordinates to a temp variable in case the user places the boat out of bounds
                        tempX = convertX;
                        tempY = convertY;

                        // Add the coordinates @see BoardView
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);

                        /* AIRCRAFT */
                        switch (boatWeAreDragging) {
                            case "aircraft":
                                if (gameModel.getHumanPlayer().aircraft.isPlaced()) { // Boat has already been placed
                                    gameModel.getHumanPlayer().gameBoard.removeCoordinates(gameModel.getHumanPlayer().aircraft.map);
                                    // Delete all coordinates for this ship
                                    gameModel.getHumanPlayer().aircraft.clearCoordinates();
                                }
                                // Get actual Row from the @see BoardView based on x
                                tempX = gameModel.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = gameModel.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 5; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 1;
                                    }
                                }

                                gameModel.getHumanPlayer().aircraft.map = boatsCoordinates;
                                gameModel.getHumanPlayer().gameBoard.addCoordinates(gameModel.getHumanPlayer().aircraft.map);
                                gameModel.getHumanPlayer().aircraft.setPlaced(true);
                                break;

                            case "battleship":
                                if (gameModel.getHumanPlayer().battleship.isPlaced()) { // If boat is already placed
                                    gameModel.getHumanPlayer().gameBoard.removeCoordinates(gameModel.getHumanPlayer().battleship.map);
                                    // Delete all coordinates for this ship
                                    gameModel.getHumanPlayer().battleship.clearCoordinates();
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = gameModel.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = gameModel.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 4; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 2;
                                    }
                                }
                                gameModel.getHumanPlayer().battleship.map = boatsCoordinates;
                                gameModel.getHumanPlayer().gameBoard.addCoordinates(gameModel.getHumanPlayer().battleship.map);
                                gameModel.getHumanPlayer().battleship.setPlaced(true);
                                break;

                            case "destroyer":
                                if (gameModel.getHumanPlayer().destroyer.isPlaced()) { // If boat is already placed
                                    gameModel.getHumanPlayer().gameBoard.removeCoordinates(gameModel.getHumanPlayer().destroyer.map);
                                    // Delete all coordinates for this ship
                                    gameModel.getHumanPlayer().destroyer.clearCoordinates();
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = gameModel.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = gameModel.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 3; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 3;
                                    }
                                }
                                gameModel.getHumanPlayer().destroyer.map = boatsCoordinates;
                                gameModel.getHumanPlayer().gameBoard.addCoordinates(gameModel.getHumanPlayer().destroyer.map);
                                gameModel.getHumanPlayer().destroyer.setPlaced(true);
                                break;

                            case "submarine":
                                if (gameModel.getHumanPlayer().submarine.isPlaced()) { // If boat is already placed
                                    gameModel.getHumanPlayer().gameBoard.removeCoordinates(gameModel.getHumanPlayer().submarine.map);
                                    // Hence, delete all coordinates for this ship
                                    gameModel.getHumanPlayer().submarine.clearCoordinates();
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = gameModel.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = gameModel.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 3; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 4;
                                    }
                                }
                                gameModel.getHumanPlayer().submarine.map = boatsCoordinates;
                                gameModel.getHumanPlayer().gameBoard.addCoordinates(gameModel.getHumanPlayer().submarine.map);
                                gameModel.getHumanPlayer().submarine.setPlaced(true);
                                break;

                            case "patrol":
                                if (gameModel.getHumanPlayer().patrol.isPlaced()) { // If boat is already placed
                                    gameModel.getHumanPlayer().gameBoard.removeCoordinates(gameModel.getHumanPlayer().patrol.map);
                                    gameModel.getHumanPlayer().patrol.clearCoordinates(); // Hence, delete all coordinates for this ship
                                    // Save the coordinates for other boats
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = gameModel.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = gameModel.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 2; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 5;
                                    }
                                }
                                gameModel.getHumanPlayer().patrol.map = boatsCoordinates;
                                gameModel.getHumanPlayer().gameBoard.addCoordinates(gameModel.getHumanPlayer().patrol.map);
                                gameModel.getHumanPlayer().patrol.setPlaced(true);
                                break;
                        }

                    } else { // OUT OF BOUNDS, RESET THE BOAT COORDINATES TO PREVIOUS LOCATION
                        //noinspection deprecation
                        v.setBackgroundDrawable(reject);
                        view.setX(tempX - 50);
                        view.setY(tempY - 50);
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                        toast("Out of bounds");
                        // v.setBackgroundDrawable(error);
                        gameModel.getHumanPlayer().boardView.invalidate();
                        getResult = true;
                    }
                    Log.w("Lost.getX()", String.valueOf(event.getX()) + "Lost.getY()" + String.valueOf(event.getY()));
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    if (!getResult) {
                        toast("Can't place here");
                    }
                    view = (View) event.getLocalState();
                    view.setVisibility(View.VISIBLE);
                default:
                    gameModel.getHumanPlayer().boardView.coordinatesOfHumanShips = gameModel.getHumanPlayer().gameBoard.grid; // Prevents from drawing multiple times when the
                    // user changes

                    gameModel.getHumanPlayer().boardView.invalidate();
                    break;
            }

            return true;
        }
    }

    private class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                } else {
                    //noinspection deprecation
                    view.startDrag(data, shadowBuilder, view, 0);
                }
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }

    }
}

