package edu.utep.cs.cs4330.battleship;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.*;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_ship
 */

@SuppressWarnings("ALL")
public class GameController extends Activity {
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static MediaPlayer mp;                     // For boats sound effects
    private static MediaPlayer music;                  // For music background
    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                toast("Device found");
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                toast("Device is now connected");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                toast("Done searching");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                toast("Device is about to disconnect");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                toast("Device has been disconnected");
            }
        }
    };
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int) msg.arg1;
            int end = (int) msg.arg2;

            switch (msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    break;
            }
        }
    };
    private RetainedFragment mRetainedFragment; // If the screen is changed we can restore data and layouts
    private String fontPath;
    private Game game = new Game();
    private BluetoothDevice mDevice;
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mmSocket;

    /* END GETTERS AND SETTERS */

    /* BEGIN GETTERS AND SETTERS */
    private String getFontPath() {
        return fontPath;
    }

    private void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    /**
     * @param savedInstanceState The creation of this mobile application. It calls a method that displays the
     *                           an aesthetic background image with the title of Battleship in 8-bit style.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFontPath("fonts/brandonlight.TTF");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        mRetainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new RetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
            mRetainedFragment.setData(game);
        }
        launchHomeView();   // By default, this activity will always display until an event occurs.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mDevice = device;
            }
            super.onActivityResult(requestCode, resultCode, data);
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }
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
            if (game.getMusicTimer() > 0) {
                music.seekTo(game.getMusicTimer());
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
        if (game.isMediaPlaying(music) == true) {
            music.pause();
            game.setMusicTimer(music.getCurrentPosition());
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (music != null && !music.isPlaying())
            music.start();
        super.onResume();
    }

    /* BEGIN SCREEN CONFIGURATIONS LOGIC */

    @Override
    public void onDestroy() {
        if (music != null) {
            music.stop();
            music.release();
            music = null;
        }
        super.onDestroy();
    }

    /**
     * @param newConfig When the user rotates their phone either from portrait to landscape or landscape to portrait,
     *                  often times activities are destroyed. This method stores the current view the user was currently
     *                  on and if applicable stores the other data i.e coordinates boats, etc.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Gets the state the user was on before the screen was oriented
        game = mRetainedFragment.getCurrentGameState();

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
                        playGameView(game.getHumanPlayer().boardView);
                        break;
                }
                //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
                break;

            case Configuration.ORIENTATION_PORTRAIT:
                game = mRetainedFragment.getCurrentGameState();
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
                        playGameView(game.getHumanPlayer().boardView);
                        break;
                }
                //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
                break;
        }
    }

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
                        game.setTypeOfGame("1 VS 1");
                        checkBluetoothConnection();
                    }
                });
                onlineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        game.setTypeOfGame("1 VS PC");
                        placeBoatsView();
                    }
                });
            }
        });
    }
    /* END SCREEN CONFIGURATIONS LOGIC */

/* BEGIN VIEWS */

    private void checkBluetoothConnection() {

        if (mBluetoothAdapter == null) {
            toast("Device does not support Blueetooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                toast("Bluetooth is not on");
                // OPEN DEFAULT NETWORK SETTINGS FROM PHONE DEVICE
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);
            }
            if (mBluetoothAdapter.isEnabled()) {
                toast("Bluetooth is already on");
            }
        }
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
                game.setDifficulty("easy");
                placeBoatsView();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setDifficulty("medium");
                placeBoatsView();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setDifficulty("hard");
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
        game.getHumanPlayer().boardView = (BoardView) findViewById(R.id.humanBoardView);
        game.getHumanPlayer().boardView.setBoard(game.getHumanPlayer().gameBoard);

        // This means the user had already placed boats on the grid but decided to go back to this view and perhaps
        // change the boats.
        if (game.getHumanPlayer().gameBoard.grid != null) {
            game.getHumanPlayer().boardView.coordinatesOfHumanShips = game.getHumanPlayer().gameBoard.grid;
            game.getHumanPlayer().boardView.invalidate();
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
                playGameView(game.getHumanPlayer().boardView);
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
        game.getHumanPlayer().boardView = (BoardView) findViewById(R.id.humanBoard);
        game.getHumanPlayer().boardView.setBoard(game.getHumanPlayer().gameBoard);

        // Get the coordinates from the previous activity to this activity
        game.getHumanPlayer().boardView.coordinatesOfHumanShips = copyOfHumanBoard.coordinatesOfHumanShips;
        /* End Human Board */

        /* Begin Computer Stuff GameController */
        final Context activityContext = this;

        game.getComputerPlayer().boardView = (BoardView) findViewById(R.id.computerBoard);
        game.getComputerPlayer().boardView.setBoard(game.getComputerPlayer().gameBoard);

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

        /* End Computer Stuff GameController*/
        Log.w("Computers board", Arrays.deepToString(game.getComputerPlayer().gameBoard.grid));
        Log.w("Humans board", Arrays.deepToString(game.getHumanPlayer().gameBoard.grid));
        game.getComputerPlayer().boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            /* After player taps on computers board */
            @Override
            public void onTouch(int x, int y) {

                // Human shoots at Computers board
                if (game.getHumanPlayer().shootsAt(game.getComputerPlayer().gameBoard, x, y)) { // Human hits a boat, paint red
                    makeExplosionSound(activityContext);
                    toast("HIT");
                    game.getComputerPlayer().boardView.gameCoordinates[x][y] = 8; // Set it to 8 to indicate it is a hit
                } else { // Human misses, paint computers board white
                    game.getComputerPlayer().boardView.gameCoordinates[x][y] = -9; // Set it to -9 to indicate it is a miss
                    toast("That was close!");
                    makeMissedSound(activityContext);
                    game.getHumanPlayer().shoots(); // Increment counter for # of shots
                    counter.setText(String.valueOf("Number of Shots: " + game.getHumanPlayer().getNumberOfShots()));

                    // COMPUTER SHOOT AT HUMAN BOARD
                    int randomX = generateRandomCoordinate(); // Generate random coordinates
                    int randomY = generateRandomCoordinate(); // Generate random coordinates

                    if (game.getComputerPlayer().shootsAt(game.getHumanPlayer().gameBoard, randomX, randomY)) {
                        makeExplosionSound(activityContext);
                        toast("HIT");
                        game.getHumanPlayer().boardView.gameCoordinates[randomX][randomY] = 8; // Set it to 8 to indicate it is a hit
                        game.getHumanPlayer().boardView.invalidate();
                    } else {
                        game.getHumanPlayer().boardView.gameCoordinates[randomX][randomY] = -9; // Set it to -9 to indicate it is a miss
                        toast("That was close!");
                        makeMissedSound(activityContext);
                    }
                    game.getHumanPlayer().boardView.invalidate();
                }
                mRetainedFragment.setData(game);
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
        new CountDownTimer(500, 2000) {
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
                    launchHomeView();
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
                                toast("Quiting GameController!");
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
                builder.setMessage("Are you sure you want to start a new GameController?");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                toast("New GameController successfully created!");
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

    /**
     * Created by oscarricaud on 4/8/17.
     */
    public class RetainedFragment extends Fragment {

        // data object we want to retain
        private Game gameState = new Game();
        private String currentView;

        // this method is only called once for this fragment
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // retain this fragment
            setRetainInstance(true);
        }

        Game getCurrentGameState() {
            return gameState;
        }

        public void setData(Game data) {
            this.gameState = data;
        }

        String getCurrentView() {
            return currentView;
        }

        void setCurrentView(String currentView) {
            this.currentView = currentView;
        }
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
                                if (game.getHumanPlayer().aircraft.isPlaced()) { // Boat has already been placed
                                    game.getHumanPlayer().gameBoard.removeCoordinates(game.getHumanPlayer().aircraft.map);
                                    // Delete all coordinates for this ship
                                    game.getHumanPlayer().aircraft.clearCoordinates();
                                }
                                // Get actual Row from the @see BoardView based on x
                                tempX = game.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = game.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 5; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 1;
                                    }
                                }

                                game.getHumanPlayer().aircraft.map = boatsCoordinates;
                                game.getHumanPlayer().gameBoard.addCoordinates(game.getHumanPlayer().aircraft.map);
                                game.getHumanPlayer().aircraft.setPlaced(true);
                                break;

                            case "battleship":
                                if (game.getHumanPlayer().battleship.isPlaced()) { // If boat is already placed
                                    game.getHumanPlayer().gameBoard.removeCoordinates(game.getHumanPlayer().battleship.map);
                                    // Delete all coordinates for this ship
                                    game.getHumanPlayer().battleship.clearCoordinates();
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = game.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = game.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 4; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 2;
                                    }
                                }
                                game.getHumanPlayer().battleship.map = boatsCoordinates;
                                game.getHumanPlayer().gameBoard.addCoordinates(game.getHumanPlayer().battleship.map);
                                game.getHumanPlayer().battleship.setPlaced(true);
                                break;

                            case "destroyer":
                                if (game.getHumanPlayer().destroyer.isPlaced()) { // If boat is already placed
                                    game.getHumanPlayer().gameBoard.removeCoordinates(game.getHumanPlayer().destroyer.map);
                                    // Delete all coordinates for this ship
                                    game.getHumanPlayer().destroyer.clearCoordinates();
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = game.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = game.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 3; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 3;
                                    }
                                }
                                game.getHumanPlayer().destroyer.map = boatsCoordinates;
                                game.getHumanPlayer().gameBoard.addCoordinates(game.getHumanPlayer().destroyer.map);
                                game.getHumanPlayer().destroyer.setPlaced(true);
                                break;

                            case "submarine":
                                if (game.getHumanPlayer().submarine.isPlaced()) { // If boat is already placed
                                    game.getHumanPlayer().gameBoard.removeCoordinates(game.getHumanPlayer().submarine.map);
                                    // Hence, delete all coordinates for this ship
                                    game.getHumanPlayer().submarine.clearCoordinates();
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = game.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = game.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 3; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 4;
                                    }
                                }
                                game.getHumanPlayer().submarine.map = boatsCoordinates;
                                game.getHumanPlayer().gameBoard.addCoordinates(game.getHumanPlayer().submarine.map);
                                game.getHumanPlayer().submarine.setPlaced(true);
                                break;

                            case "patrol":
                                if (game.getHumanPlayer().patrol.isPlaced()) { // If boat is already placed
                                    game.getHumanPlayer().gameBoard.removeCoordinates(game.getHumanPlayer().patrol.map);
                                    game.getHumanPlayer().patrol.clearCoordinates(); // Hence, delete all coordinates for this ship
                                    // Save the coordinates for other boats
                                }

                                // Get actual Row from the @see BoardView based on x
                                tempX = game.getHumanPlayer().boardView.locateX(convertX);
                                // Get actual Column from the @see BoardView based on y
                                tempY = game.getHumanPlayer().boardView.locateY(convertY);

                                for (int i = 0; i < 2; i++) {
                                    if (tempX + i >= 0 && tempX + i < 10 && tempY < 10 && tempY >= 0) {
                                        boatsCoordinates[tempX + i][tempY] = 5;
                                    }
                                }
                                game.getHumanPlayer().patrol.map = boatsCoordinates;
                                game.getHumanPlayer().gameBoard.addCoordinates(game.getHumanPlayer().patrol.map);
                                game.getHumanPlayer().patrol.setPlaced(true);
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
                        game.getHumanPlayer().boardView.invalidate();
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
                    game.getHumanPlayer().boardView.coordinatesOfHumanShips = game.getHumanPlayer().gameBoard.grid; // Prevents from drawing multiple times when the
                    // user changes

                    game.getHumanPlayer().boardView.invalidate();
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

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    for (int i = begin; i < bytes; i++) {
                        if (buffer[i] == "#".getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if (i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}

