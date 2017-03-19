package edu.utep.cs.cs4330.battleship;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// Controller
/**
 * @author Oscar Ivan Ricaud
 * @version 1.0
 * Last update: 02/23/2017
 */
public class GameController extends AppCompatActivity {
    private Board board;
    private BoardView boardView;
    private int countShots = 0;
    private MediaPlayer mp;
    private TextView counter;
    private Font eightBitFont = new Font("fonts/eightbit.TTF");
    private Music shipSound = new Music();

    private Music gamePlayMusic = new Music();
    private String difficulty;
    /** This is the main controller class and handles the creation of multiple ships and board.
     //    *  @see Ship.java
     //   *  @see BoardView.java
     //   *  @see Board.java
     *  for more information.
     *
     *  At random, ships are placed either horizontally or vertically on a 10x10 board.
     *  The user is able to interact with this board and creates (x,y) coordinates.
     *  The user coordinates are compared to the coordinates from all boats that are randomly placed
     *  on the board. If the user hits a boat the method onDraw is invoked from the
     //   *  @see BoardView.java
     *  and colors a red circle the position of the boats, else colors a white circle indicating the
     *  user missed.
     *
     * @param savedInstanceState is the starting state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
            {
                launchView(); // Launch the first activity, the starting state, s0.
            }else{
                String method = extras.getString("methodName");

                if (method.equals("computerPlaceBoatsView"))
                {
                    computerPlaceBoatsView(); // This means that the human has already placed the boats
                                            // and chosen a difficulty level
                }
            }
        }
    }

    /**
     * This is state 0, I will represent states such as s0, s1, s2, ... sn. Hopefully this makes it
     * easier to understand.
     *
     * The launch view is the beginning to this mobile application. It launches the home screen
     * and allows the user to begin a new game.
     *
     * s0 waits until the user clicks on the start button if the user clicks on the start
     * button the state diagram looks like the following: s0->s1, where s1 awaits for the user
     * input to choose a level of difficulty for the upcoming game.
     */
    private void launchView(){
        setContentView(R.layout.home);
        gamePlayMusic.playMusic(this);
        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip); // Change font
        eightBitFont.changeFont(this, battleshipLabel);

        // Begin to the next activity, placing boats on the map
        Button startButton = (Button) findViewById(R.id.start);
        eightBitFont.changeFont(this, startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLevelView(); // Ask the user what Level of difficulty do they want to play on.
            }
        });
    }
    /** s1, Changes the current view where the user is able to choose from 3 buttons the following:
     *  easy, medium or hard, @see layout/activity_level for more details.
     */
    private void chooseLevelView() {
        setContentView(R.layout.activity_level);
        Button easy = (Button) findViewById(R.id.easy);
        Button medium = (Button) findViewById(R.id.medium);
        Button hard = (Button) findViewById(R.id.hard);
        TextView chooseLevel = (TextView) findViewById(R.id.chooseDifficulty);

        // Change font to a cooler 8-bit font.
        eightBitFont.changeFont(this, easy);
        eightBitFont.changeFont(this, medium);
        eightBitFont.changeFont(this, hard);
        eightBitFont.changeFont(this, chooseLevel);

        // Wait for the user input
        easy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("easy");
                humanPlaceBoatsView();  // Takes the user to @see Place_Ship to place ships on the grid.
            }
        });
        medium.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("medium");
                humanPlaceBoatsView(); // Takes the user to @see Place_Ship to place ships on the grid.
            }
        });
        hard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("hard");
                humanPlaceBoatsView(); // Takes the user to @see Place_Ship to place ships on the grid.
            }
        });
    }
    /** Where the magic happens. Aka the function that allows the human to place boats on board view.
     *  @see Place_Ship for more details.
     */
    private void humanPlaceBoatsView() {
        // The following is how you send data to other classes.
        Intent intent = new Intent(GameController.this, Place_Ship.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        GameController.this.startActivity(intent);

        /** Fading Transition Effect */
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        //computerPlaceBoatsView();
    }

    /**
     * @return this returns the difficulty the user chose, which is later retrieved to be displayed
     * on the android phone.
     */
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Changes the content view to a more grey, unnerving background. Also changes the song to more
     * melancholy while attempting to petrify the user.
     *
     * Also it is the function where the computer places the boats on the grid at random.
     * Currently I would personally say that it is easy. Most of the times the boats will
     * be adjacent to each other.
     */
    private void computerPlaceBoatsView() {
        setContentView(R.layout.activity_game);
        board = new Board(10);
        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);

        // Below we define the boats that will be placed on the board
        final Ship aircraft = new Ship(5, "aircraft", "Computer");
        final Ship battleship = new Ship(4, "battleship", "Computer");
        final Ship destroyer = new Ship(3, "destroyer", "Computer");
        final Ship submarine = new Ship(3, "submarine", "Computer");
        final Ship patrol = new Ship(2, "patrol", "Computer");

        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip);
        eightBitFont.changeFont(this, battleshipLabel); // Change font

        // The counter displays the number of shots in the UI, the user has tapped on the board.
        counter = (TextView) findViewById(R.id.countOfHits);
        eightBitFont.changeFont(this, counter); // Change font
        countShots = 0;
        setCountShots(0);
        final Context activityContext = this;

        // Listen for the user input
        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                setCountShots(countShots+1);
                counter.setText(String.valueOf("Number of Shots: " + getCountShots()));

                // Compare the coordinates the user just touched with any of the boats that are placed
                // on the board. Then either play a missed or explosion sound. When the boat sinks
                // play a louder explosion.
                if(isItAHit(aircraft.getCoordinates(), x, y)){
                    shipSound.makeExplosionSound(activityContext);
                    aircraft.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(aircraft.getHit() == 5){
                        toast("Aircraft SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                }
                else if(isItAHit(battleship.getCoordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    battleship.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(battleship.getHit() == 4){
                        toast("Battleship SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                }
                else if(isItAHit(destroyer.getCoordinates(), x, y)){
                    shipSound.makeExplosionSound(activityContext);
                    destroyer.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(destroyer.getHit() == 3){
                        toast("Destroyer SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                }
                else if(isItAHit(submarine.getCoordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    submarine.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(submarine.getHit() == 3){
                        toast("Submarine SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                }
                else if(isItAHit(patrol.getCoordinates(), x, y)) {
                    shipSound.makeExplosionSound(activityContext);
                    patrol.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(patrol.getHit() == 2){
                        toast("Patrol SUNK");
                        shipSound.makeLouderExplosion(activityContext);
                    }
                }
                else{
                    boardView.setxMiss(x);
                    boardView.setyMiss(y);
                    toast("That was close!");
                    shipSound.makeMissedSound(activityContext);
                }
            }
        });

        final Context context = this;

        // Start a new game when clicked button
        Button newButton = (Button) findViewById(R.id.newButton);
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
                                Intent intent = new Intent(GameController.this, Place_Ship.class);
                                GameController.this.startActivity(intent);
                                GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

        Button quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener(){
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
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });
        eightBitFont.changeFont(this, newButton);
        eightBitFont.changeFont(this, quitButton);
    }

    /**
     * @param coordinates are the coordinates from the user.
     * @param x is the number of rows - 1.
     * @param y is the number of columns - 1.
     * @return  If the user hits a boat return true else false.
     */
    private boolean isItAHit(int[][] coordinates, int x, int y) {
        if(coordinates[x][y] == 1){
            return true;
        }
        return false;
    }
    /** Show a toast message. */
    protected  void toast(String msg) {
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
    /**
     * @return the number of shots the user has shot
     */
    public int getCountShots() {
        return countShots;
    }
    /**
     * @param countShots this method is only used when the program is in the starting state.
     *                   By default this number of shots = 0.
     */
    public void setCountShots(int countShots) {
        this.countShots = countShots;
    }
}


