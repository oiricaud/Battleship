package edu.utep.cs.cs4330.battleship;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_ship
 */

public class GameView extends AppCompatActivity {
    private MediaPlayer mp; // For music background
    private String fontPath;

    /* Begin Fields for Human */
    private Board board = new Board(10);
    private BoardView humanBoardView;
    private BoardView humanBoardViewFinal;
    private Player humanPlayer = new Player("Human", board);
    /* End Fields for Human */

    /* Begin Fields for AI */
    private BoardView computerBoardView;
    private int countShots = 0;
    private TextView counter;
    private Player computerPlayer = new Player("Computer", board);
    /* End Fields for AI */

    private boolean getResult = false; // Determine if the an image object has been dragged

    private String getFontPath() {
        return fontPath;
    }

    private void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

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

        // The following loads the corresponding views. This class gets called from @see GameController.
        if (getIntent().getExtras() != null) {
            setFontPath("fonts/eightbit.TTF");
            Bundle extras = getIntent().getExtras();
            String viewToLaunch = extras.getString("viewWeWantToLaunch"); // Look for YOUR KEY, variable you're receiving

            if (viewToLaunch.equals("launchHomeView")) {
                launchHomeView(); // Launch the very first activity of this application
            }
            if (viewToLaunch.equals("chooseLevelView")) {
                chooseLevelView(); // The creation of this activity
            }
            if (viewToLaunch.equals("placeBoatsView")) {
                placeBoatsView(); // Loads the view that allows the user to place boats.
            }
        }
    }

    /**
     * This method launches the home.xml and waits for the user to begin a new game.
     */
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
                callTheController("chooseLevelController");
            }
        });
    }

    /**
     * This method launches the activity_level.xml and waits for the user to enter a
     * difficulty level.
     */
    private void chooseLevelView() {
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
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTheController("placeBoatsController", "easy");
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTheController("placeBoatsController", "medium");
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTheController("placeBoatsController", "hard");
            }
        });
    }

    /**
     * This method sets the toolbar for the user to tap on the board to place the boats of the ships
     */
    private void placeBoatsView() {
        setContentView(R.layout.activity_human_game);
        humanBoardView = (BoardView) findViewById(R.id.humanBoardView2);
        humanBoardView.setBoard(humanPlayer.getPlayerBoard());

        /* Set up the toolbar and define it's title */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("Place Boats");

        /* Get the image objects */
        ImageView aircraft = (ImageView) findViewById(R.id.aircraft);
        ImageView battleship = (ImageView) findViewById(R.id.battleship);
        ImageView destroyer = (ImageView) findViewById(R.id.destroyer);
        ImageView submarine = (ImageView) findViewById(R.id.submarine);
        ImageView patrol = (ImageView) findViewById(R.id.patrol);

        /* Allow these boat images to be draggable, listen when they are touched */
        aircraft.setOnTouchListener(new MyTouchListener());
        battleship.setOnTouchListener(new MyTouchListener());
        destroyer.setOnTouchListener(new MyTouchListener());
        submarine.setOnTouchListener(new MyTouchListener());
        patrol.setOnTouchListener(new MyTouchListener());

        /* Define the particular location where these boat images are allowed to be dragged onto */
        findViewById(R.id.humanBoardPlacer).setOnDragListener(new MyDragListener());

        Button next = (Button) findViewById(R.id.next);
        changeFont(this, next);
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computerBoardView(humanBoardView);
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item are the different ships the user can tap onto.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Log.w("humanPlayer.aircraft", humanPlayer.getTypeOfPlayer());
        if (item.getItemId() == R.id.itemAircraft) {

            //noinspection ConstantConditions
            getSupportActionBar().setTitle("Tap on Grid to Place Aircraft");
            humanBoardView.addBoardTouchListener(new BoardView.BoardTouchListener() {

                @Override
                public void onTouch(int x, int y) {
                }
            });
        }

        return true;

    }

    /**
     * @param whatControllerAreWeCalling is the argument we are taking to obtain the corresponding
     *                                   controller @see GameController for more details.
     */
    private void callTheController(String whatControllerAreWeCalling) {
        Intent intent = new Intent(GameView.this, edu.utep.cs.cs4330.battleship.GameController.class);
        intent.putExtra("controllerName", whatControllerAreWeCalling);
        GameView.this.startActivity(intent);
        fadingTransition(); // Fading Transition Effect
    }

    /**
     * @param whatControllerAreWeCalling is the argument we are taking to obtain the corresponding
     *                                   controller @see GameController for more details.
     * @param stringWeWantToPass         is the string we are passing to the @GameController.
     */
    private void callTheController(String whatControllerAreWeCalling, String stringWeWantToPass) {
        Intent intent = new Intent(GameView.this, edu.utep.cs.cs4330.battleship.GameController.class);
        intent.putExtra("controllerName", whatControllerAreWeCalling);
        intent.putExtra("difficulty", stringWeWantToPass);
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
     * At random, ships are placed either horizontally or vertically on a 10x10 board.
     * The user is able to interact with this board and creates (x,y) coordinates.
     * The user coordinates are compared to the coordinates from all boats that are randomly placed
     * on the board. If the user hits a boat the method onDraw is invoked from the
     *
     * @see BoardView.java
     * and colors a red circle the position of the boats, else colors a white circle indicating the
     * user missed.
     */
    private void computerBoardView(BoardView copyOfHumanBoard) {
        setContentView(R.layout.current_game);

        /* Define Human Board */
        final Board humanBoardFinal = new Board(10);
        humanBoardViewFinal = (BoardView) findViewById(R.id.humanBoard);
        humanBoardViewFinal.setBoard(humanBoardFinal);

        // Get the coordinates from the previous activity to this activity
        for (int i = 0; i < copyOfHumanBoard.getxCoordinate().size(); i++) {
            humanBoardViewFinal.setxCoordinate(copyOfHumanBoard.getxCoordinate().get(i));
            humanBoardViewFinal.setyCoordinate(copyOfHumanBoard.getyCoordinate().get(i));
        }
        /* End Human Board */

        /* Begin Computer Stuff Game */
        final Context activityContext = this;
        final Board computerBoard = new Board(10);
        computerBoardView = (BoardView) findViewById(R.id.computerBoard);
        computerBoardView.setBoard(computerBoard);


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

        /* End Computer Stuff Game*/
        Log.w("Computer's board", Arrays.deepToString(computerPlayer.boardGrid));
        computerBoardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                // The counter displays the number of shots in the UI, the user has tapped on the board.
                computerPlayer.shoots();
                counter.setText(String.valueOf("Number of Shots: " + computerPlayer.getNumberOfShots()));
                // Compare the coordinates the user just touched with any of the boats that are placed
                // on the board. Then either play a missed or explosion sound. When the boat sinks
                // play a louder explosion.
                if (computerPlayer.shootShip(x, y)) {
                    makeExplosionSound(activityContext);
                    computerBoardView.setxHit(x);
                    computerBoardView.setyHit(y);
                    toast("KA-POW");
                    /*
                    if (computerPlayer.aircraft.getHit() == 5) {
                        toast("Aircraft SUNK");
                        makeLouderExplosion(activityContext);
                    } */
                } else {
                    computerBoardView.setxMiss(x);
                    computerBoardView.setyMiss(y);
                    toast("That was close!");
                    makeMissedSound(activityContext);

                    toast("Computer Turn");
                    /* Computers TURN  shoot at human board */
                    int randX = generateRandomCoordinate();
                    int randY = generateRandomCoordinate();
                    Log.w("randX", String.valueOf(randX));
                    Log.w("randY", String.valueOf(randY));
                    if (isItAHit(humanPlayer.aircraft.gethumanSetCoordinates(), randX, randY)) {
                        makeExplosionSound(activityContext);
                        humanPlayer.aircraft.hit();
                        humanBoardViewFinal.setxHit(randX);
                        humanBoardViewFinal.setyHit(randY);
                        toast("Computer shoots");
                        if (humanPlayer.aircraft.getHit() == 5) {
                            toast("Aircraft SUNK");
                            makeLouderExplosion(activityContext);
                        }
                        humanBoardViewFinal.invalidate();
                    } else if (isItAHit(humanPlayer.battleship.gethumanSetCoordinates(), randX, randY)) {
                        makeExplosionSound(activityContext);
                        humanPlayer.battleship.hit();
                        humanBoardViewFinal.setxHit(randX);
                        humanBoardViewFinal.setyHit(randY);
                        toast("KA-POW");

                        if (humanPlayer.battleship.getHit() == 4) {
                            toast("Battleship SUNK");
                            makeLouderExplosion(activityContext);
                        }
                        humanBoardViewFinal.invalidate();
                    } else if (isItAHit(humanPlayer.destroyer.gethumanSetCoordinates(), randX, randY)) {
                        makeExplosionSound(activityContext);
                        humanPlayer.destroyer.hit();
                        humanBoardViewFinal.setxHit(randX);
                        humanBoardViewFinal.setyHit(randY);
                        toast("KA-POW");
                        if (humanPlayer.destroyer.getHit() == 4) {
                            toast("destroyer SUNK");
                            makeLouderExplosion(activityContext);
                        }
                        humanBoardViewFinal.invalidate();
                    } else if (isItAHit(humanPlayer.destroyer.gethumanSetCoordinates(), randX, randY)) {
                        makeExplosionSound(activityContext);
                        humanPlayer.destroyer.hit();
                        humanBoardViewFinal.setxHit(randX);
                        humanBoardViewFinal.setyHit(randY);
                        toast("KA-POW");
                        if (humanPlayer.destroyer.getHit() == 3) {
                            toast("destroyer SUNK");
                            makeLouderExplosion(activityContext);
                        }
                        humanBoardViewFinal.invalidate();
                    } else if (isItAHit(humanPlayer.submarine.gethumanSetCoordinates(), randX, randY)) {
                        makeExplosionSound(activityContext);
                        humanPlayer.submarine.hit();
                        humanBoardViewFinal.setxHit(randX);
                        humanBoardViewFinal.setyHit(randY);
                        toast("KA-POW");
                        if (humanPlayer.submarine.getHit() == 2) {
                            toast("submarine SUNK");
                            makeLouderExplosion(activityContext);
                        }
                        humanBoardViewFinal.invalidate();
                    } else {
                        humanBoardViewFinal.setxMiss(randX);
                        humanBoardViewFinal.setyMiss(randY);
                        toast("That was close!");
                        makeMissedSound(activityContext);
                        humanBoardViewFinal.invalidate();
                    }
                }
            }
        });
    }

    private int generateRandomCoordinate() {
        Random random = new Random();
        return random.nextInt(9 + 1);
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

    /**
     * @param quitButton Where is the type of button
     * @param context    when the user hits quit, the user is sent back to the first activity
     */
    public void quitActivity(Button quitButton, final Context context) {
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
                                callTheController("chooseLevelController");
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

    /**
     * Plays at random 3 default game songs. Makes a swish noise when the player misses a shot.
     *
     * @param context is the activity context
     */
    public void makeMissedSound(Context context) {
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
    public void makeExplosionSound(Context context) {
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

    /**
     * Changes the font of the activity
     *
     * @param context  Is the
     * @param textView the view we want to change font to
     */
    public void changeFont(Context context, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), getFontPath());
        textView.setTypeface(typeface);
    }

    private class MyDragListener implements View.OnDragListener {
        // Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable accept = getResources().getDrawable(R.drawable.accept);
        Drawable reject = getResources().getDrawable(R.drawable.reject);
        Drawable neutral = getResources().getDrawable(R.drawable.neutral);
        Drawable board_color = getResources().getDrawable(R.drawable.board_color);
        // Drawable normalShape = getResources().getDrawable(R.drawable.board_color);
        // Drawable error = getResources().getDrawable(R.drawable.error_placement_boat);

        // Make images smaller
        int width = 100;
        int height = 100;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
        private int tempX = 0;
        private int tempY = 0;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int sdk = android.os.Build.VERSION.SDK_INT;
            v.setBackground(board_color);
            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:
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
                        if (boatWeAreDragging.equals("aircraft")) {
                            if (humanPlayer.aircraft.isPlaced()) { // Boat has already been placed
                                humanPlayer.removeCoordinates(humanPlayer.aircraft.map);
                                // Delete all coordinates for this ship
                                humanPlayer.aircraft.clearCoordinates();
                            }
                            // Get actual Row from the @see BoardView based on x
                            int tempX = humanBoardView.locateX(convertX);
                            // Get actual Column from the @see BoardView based on y
                            int tempY = humanBoardView.locateY(convertY);

                            for (int i = 0; i < 5; i++) {
                                if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                    boatsCoordinates[tempX + i][tempY] = 1;
                                }
                            }
                            humanPlayer.aircraft.map = boatsCoordinates;
                            humanPlayer.addCoordinates(humanPlayer.aircraft.map);
                            humanPlayer.aircraft.setPlaced(true);
                        }

                        /* BATTLESHIP */
                        if (boatWeAreDragging.equals("battleship")) {
                            if (humanPlayer.battleship.isPlaced()) { // If boat is already placed
                                humanPlayer.removeCoordinates(humanPlayer.battleship.map);
                                // Delete all coordinates for this ship
                                humanPlayer.battleship.clearCoordinates();
                            }

                            // Get actual Row from the @see BoardView based on x
                            int tempX = humanBoardView.locateX(convertX);
                            // Get actual Column from the @see BoardView based on y
                            int tempY = humanBoardView.locateY(convertY);

                            for (int i = 0; i < 4; i++) {
                                if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                    boatsCoordinates[tempX + i][tempY] = 2;
                                }
                            }
                            humanPlayer.battleship.map = boatsCoordinates;
                            humanPlayer.addCoordinates(humanPlayer.battleship.map);
                            humanPlayer.battleship.setPlaced(true);
                        }

                        /* DESTROYER */
                        if (boatWeAreDragging.equals("destroyer")) {
                            if (humanPlayer.destroyer.isPlaced()) { // If boat is already placed
                                humanPlayer.removeCoordinates(humanPlayer.destroyer.map);
                                // Delete all coordinates for this ship
                                humanPlayer.destroyer.clearCoordinates();
                            }

                            // Get actual Row from the @see BoardView based on x
                            int tempX = humanBoardView.locateX(convertX);
                            // Get actual Column from the @see BoardView based on y
                            int tempY = humanBoardView.locateY(convertY);

                            for (int i = 0; i < 3; i++) {
                                if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                    boatsCoordinates[tempX + i][tempY] = 3;
                                }
                            }
                            humanPlayer.destroyer.map = boatsCoordinates;
                            humanPlayer.addCoordinates(humanPlayer.destroyer.map);
                            humanPlayer.destroyer.setPlaced(true);
                        }

                        /* SUBMARINE */
                        if (boatWeAreDragging.equals("submarine")) {
                            if (humanPlayer.submarine.isPlaced()) { // If boat is already placed
                                humanPlayer.removeCoordinates(humanPlayer.submarine.map);
                                // Hence, delete all coordinates for this ship
                                humanPlayer.submarine.clearCoordinates();
                            }

                            // Get actual Row from the @see BoardView based on x
                            int tempX = humanBoardView.locateX(convertX);
                            // Get actual Column from the @see BoardView based on y
                            int tempY = humanBoardView.locateY(convertY);

                            for (int i = 0; i < 3; i++) {
                                if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                    boatsCoordinates[tempX + i][tempY] = 4;
                                }
                            }
                            humanPlayer.submarine.map = boatsCoordinates;
                            humanPlayer.addCoordinates(humanPlayer.submarine.map);
                            humanPlayer.submarine.setPlaced(true);
                        }

                         /* PATROL */
                        if (boatWeAreDragging.equals("patrol")) {
                            if (humanPlayer.patrol.isPlaced()) { // If boat is already placed
                                humanPlayer.removeCoordinates(humanPlayer.patrol.map);
                                humanPlayer.patrol.clearCoordinates(); // Hence, delete all coordinates for this ship
                                // Save the coordinates for other boats
                            }

                            // Get actual Row from the @see BoardView based on x
                            int tempX = humanBoardView.locateX(convertX);
                            // Get actual Column from the @see BoardView based on y
                            int tempY = humanBoardView.locateY(convertY);

                            for (int i = 0; i < 2; i++) {
                                if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                    boatsCoordinates[tempX + i][tempY] = 5;
                                }
                            }
                            humanPlayer.patrol.map = boatsCoordinates;
                            humanPlayer.addCoordinates(humanPlayer.patrol.map);
                            humanPlayer.patrol.setPlaced(true);
                        }

                    } else { // OUT OF BOUNDS, RESET THE BOAT COORDINATES TO PREVIOUS LOCATION
                        v.setBackgroundDrawable(reject);
                        view.setX(tempX - 50);
                        view.setY(tempY - 50);
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                        toast("Out of bounds");
                        // v.setBackgroundDrawable(error);
                        humanBoardView.invalidate();
                        getResult = true;
                    }
                    Log.w("Lost.getX()", String.valueOf(event.getX()) + "Lost.getY()" + String.valueOf(event.getY()));
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    if (getResult == false) {
                        toast("Can't place here");
                    }
                    view = (View) event.getLocalState();
                    view.setVisibility(View.VISIBLE);
                default:
                    humanBoardView.map = humanPlayer.boardGrid; // Prevents from drawing multiple times when the
                    // user changes
                    humanBoardView.invalidate();
                    break;
            }

            return true;
        }
    }
}

