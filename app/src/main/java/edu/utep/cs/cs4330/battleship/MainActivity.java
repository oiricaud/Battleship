package edu.utep.cs.cs4330.battleship;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Oscar Ivan Ricaud
 * @version 1.0
 * Last update: 02/12/2017
 */
public class MainActivity extends AppCompatActivity {
    private Board board;
    private BoardView boardView;
    private int countShots;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = new Board(10);
        boardView = (BoardView) findViewById(R.id.boardView);
        boardView.setBoard(board);
        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                //toast(String.format("Touched: %d, %d", x, y));
            }
        });

        final Player player = new Player();
        final TextView counter = (TextView) findViewById(R.id.countOfHits);
        countShots = 0;
        player.setUpBoats();
        setCountShots(0);
        makeShots(player, counter);

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

    private void makeShots(final Player player, final TextView counter) {

        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {
                setCountShots(countShots+1);
                counter.setText(String.valueOf("Number of Shots after: " + getCountShots()));
                //     toast(String.format("Touched: %d, %d", x, y));

                for (int i = 0; i < player.battleship.getBattleshipCoordinates().size(); i++) {
                    // (LEFTMOST, RIGHTMOST) == (x, y) == (x+1, y+1)... (x+N, y+N)
                    String leftMost = String.valueOf(player.battleship.getBattleshipCoordinates().get(i).charAt(0));
                    String rightMost = String.valueOf(player.battleship.getBattleshipCoordinates().get(i).charAt(2));

                    if ((String.valueOf(x).equals(leftMost)) && (String.valueOf(y).equals(rightMost))) { // User hits
                        Log.w("Critical hit! X:", String.valueOf(x) + "Critical hit! Y:" + String.valueOf(y));
                        player.battleship.setXandY(x, y);
                        String coordinates = x + " " + y;
                        toast(("Critical hit!"));
                        // the setNumOfHits keeps track of the number of times the boat has been hit.
                        player.battleship.setNumOfHits(coordinates);
                        player.battleship.ispositiontaken(x, y); // for the final map in @see FleetShip

                        if (!(player.battleship.isSunk())) { // When you hit the battleship
                            Log.w("Hit", "Ka-pow");
                            makeExplosionSound();
                        }
                        if (player.battleship.isSunk()) { // When you sink the boat
                            Log.w("Abort! Boat has sunk", "Ka-baam");
                            makeLouderExplosion();
                        }
                    }
                    if (!(String.valueOf(x).equals(leftMost)) && (!(String.valueOf(y).equals(rightMost)))) { // When the user misses
                        Log.w("Phew", "That was close");
                        toast(("Missed"));
                        missedSound();
                    }
                }
                player.battleship.getXandY();
            }
        });
    }

    private void missedSound() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.missed);
        mp.start();
    }

    private void makeLouderExplosion() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.sunk);
        mp.start();
    }

    private void makeExplosionSound() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.torpedo);
        mp.start();
    }

    /** Show a toast message. */
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
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
