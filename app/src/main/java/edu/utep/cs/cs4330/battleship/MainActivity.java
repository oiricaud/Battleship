package edu.utep.cs.cs4330.battleship;

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
 * Last update: 02/22/2017
 */
public class MainActivity extends AppCompatActivity {
    private Board board;
    private BoardView boardView;
    private int countShots = 0;
    private MediaPlayer mp;
    private TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = new Board(10);

        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);

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
                //board.at(x, y);
                setCountShots(countShots+1);
                counter.setText(String.valueOf("Number of Shots after: " + getCountShots()));
                if(isItAHit(battleship.getCoordinates(), x, y)
                        || isItAHit(aircraft.getCoordinates(), x, y)
                        || isItAHit(destroyer.getCoordinates(), x, y)
                        || isItAHit(submarine.getCoordinates(), x, y)
                        || isItAHit(patrol.getCoordinates(), x, y))
                {
                    boardView.setxHit(x);
                    boardView.setyHit(y);
                    toast("KA-POW");
                    makeExplosionSound();
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

