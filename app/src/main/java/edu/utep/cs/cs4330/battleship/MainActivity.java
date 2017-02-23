package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
public class MainActivity extends AppCompatActivity {
    private Board board;
    private BoardView boardView;
    private int countShots = 0;
    private MediaPlayer mp;
    private TextView counter;

    /** This is the main controller class and handles the creation of multiple ships and board.
     *  @see Ship.java
     *  @see BoardView.java
     *  @see Board.java
     *  for more information.
     *
     *  At random, ships are placed either horizontally or vertically on a 10x10 board.
     *  The user is able to interact with this board and creates (x,y) coordinates.
     *  The user coordinates are compared to the coordinates from all boats that are randomly placed
     *  on the board. If the user hits a boat the method onDraw is invoked from the
     *  @see BoardView.java
     *  and colors a red circle the position of the boats, else colors a white circle indicating the
     *  user missed.
     *
     * @param savedInstanceState is the starting state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = new Board(10);

        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);

        // Below we define the boats that will be placed on the board
        final Ship aircraft = new Ship(5, "aircraft");
        final Ship battleship = new Ship(4, "battleship");
        final Ship destroyer = new Ship(3, "destroyer");
        final Ship submarine = new Ship(3, "submarine");
        final Ship patrol = new Ship(2, "patrol");

        counter = (TextView) findViewById(R.id.countOfHits);
        countShots = 0;
        setCountShots(0);

        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                setCountShots(countShots+1);
                counter.setText(String.valueOf("Number of Shots after: " + getCountShots()));
                
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

        // Start a new game when clicked button
        Button newButton = (Button) findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("New Game successfully created!");
                restartActivity();
            }
        });
    }

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

    public int getCountShots() {
        return countShots;
    }

    public void setCountShots(int countShots) {
        this.countShots = countShots;
    }
}

