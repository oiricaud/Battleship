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
public class GameActivity extends AppCompatActivity {
    private Board board;
    private BoardView boardView;
    private int countShots = 0;
    private MediaPlayer mp;
    private TextView counter;
    private Font eightBitFont = new Font("fonts/eightbit.TTF");
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
        // We can modify this to verify if the AI and user are ready to begin the game, if so begin.
        beginGame();
    }

    private void beginGame() {
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
                    makeExplosionSound();
                    aircraft.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(aircraft.getHit() == 5){
                        toast("Aircraft SUNK");
                        makeLouderExplosion();
                    }
                }
                else if(isItAHit(battleship.getCoordinates(), x, y)) {
                    makeExplosionSound();
                    battleship.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(battleship.getHit() == 4){
                        toast("Battleship SUNK");
                        makeLouderExplosion();
                    }
                }

                else if(isItAHit(destroyer.getCoordinates(), x, y)){
                    makeExplosionSound();
                    destroyer.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(destroyer.getHit() == 3){
                        toast("Destroyer SUNK");
                        makeLouderExplosion();
                    }
                }
                else if(isItAHit(submarine.getCoordinates(), x, y)) {
                    makeExplosionSound();
                    submarine.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(submarine.getHit() == 3){
                        toast("Submarine SUNK");
                        makeLouderExplosion();
                    }
                }
                else if(isItAHit(patrol.getCoordinates(), x, y)) {
                    makeExplosionSound();
                    patrol.hit();
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    if(patrol.getHit() == 2){
                        toast("Patrol SUNK");
                        makeLouderExplosion();
                    }
                }
                else{
                    boardView.setxMiss(x);
                    boardView.setyMiss(y);
                    toast("That was close!");
                    makeMissedSound();
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to start a new Game?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                toast("New Game successfully created!");
                                restartActivity();
                                Intent intent = new Intent(GameActivity.this, Place_Ship.class);
                                GameActivity.this.startActivity(intent);
                                GameActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        Button quitButton = (Button) findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Alert Dialogue
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to quit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                toast("Quiting Game!");
                                finish();
                                Intent intent = new Intent(GameActivity.this, LaunchView.class);
                                GameActivity.this.startActivity(intent);
                                GameActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
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

    /**
     * Makes a swish noise when the player misses a shot.
     */
    private void makeMissedSound() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.missed);
        mp.start();
    }

    /**
     * Makes an explosion sound if the user hits a boat.
     */
    private void makeExplosionSound() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.torpedo);
        mp.start();
    }

    /**
     * Makes a louder explosion as the latter method.
     */
    private void makeLouderExplosion() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.sunk);
        mp.start();
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


