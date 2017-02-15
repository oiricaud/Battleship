package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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
 * Last update: 02/12/2017
 */
public class MainActivity extends AppCompatActivity {
    private Board board;
    private BoardView boardView;
    private int countShots = 0;
    private MediaPlayer mp;
    private TextView counter;
    private  Player player;
    private static final int SHORT_DELAY = 1000; // 2 seconds
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

        player = new Player();
        counter = (TextView) findViewById(R.id.countOfHits);
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

    /**
     * @param player makes shots based on its input (x,y) when it clicks on the board.
     * @param counter auto-increments each time the player makes a shot and displays it in the view.
     */
    private void makeShots(final Player player, final TextView counter) {

        boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
            @Override
            public void onTouch(int x, int y) {

                board.at(x, y, boardView);
                setCountShots(countShots+1);
                counter.setText(String.valueOf("Number of Shots after: " + getCountShots()));
                handleBattleship(boardView, x,y);
                handleAircraftCarrier(boardView, x,y);
            }
        });
    }

    private void handleAircraftCarrier(BoardView boardView, int x, int y) {
        for (int i = 0; i < player.aircraftcraftCarrier.getAircraftcraftCarrierCoordinates().size(); i++) {
            // (LEFTMOST, RIGHTMOST) == (x, y) == (x+1, y+1)... (x+N, y+N)
            String leftMost = String.valueOf(player.aircraftcraftCarrier.getAircraftcraftCarrierCoordinates().get(i).charAt(0));
            String rightMost = String.valueOf(player.aircraftcraftCarrier.getAircraftcraftCarrierCoordinates().get(i).charAt(2));
            if ((String.valueOf(x).equals(leftMost)) && (String.valueOf(y).equals(rightMost))) { // User hits

                Log.w("ACCritical hit! X:", String.valueOf(x) + "Critical hit! Y:" + String.valueOf(y));
                player.aircraftcraftCarrier.setXandY(x, y);
                String coordinates = x + " " + y;
                toast(("ACCritical hit!"));
                // the setNumOfHits keeps track of the number of times the boat has been hit.
                player.aircraftcraftCarrier.setNumOfHits(coordinates);
                player.aircraftcraftCarrier.ispositiontaken(x, y); // for the final map in @see FleetShip

                if (!(player.aircraftcraftCarrier.isSunk())) { // When you hit the battleship
                    Log.w("ACHit", "Ka-pow");
                    toast("ACKA-POW");
                    makeExplosionSound();
                    boardView.setiShot(true);
                    // board.setHitArray(666);
                    // boardView.setCoordinates(player.battleship.getBattleshipCoordinates());
                  //  boardView.invalidate(); // calls ondraw method eventually
                }
                if (player.aircraftcraftCarrier.isSunk()) { // When you sink the boat
                    Log.w("ACAbort! Boat has sunk", "Ka-baam");
                    toast("ACSUNK BATTLESHIP");
                    boardView.setiShot(true);
                    makeLouderExplosion();
                }
            }
            if (!(String.valueOf(x).equals(leftMost)) && (!(String.valueOf(y).equals(rightMost)))) { // When the user misses
                Log.w("ACPhew", "That was close");
                toast(("ACMissed"));
                missedSound();
                boardView.setiShot(false); // shot missed
            }
        }
        player.aircraftcraftCarrier.getXandY();
    }

    private void handleBattleship(BoardView boardView, int x, int y) {
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
                    toast("KA-POW");
                    makeExplosionSound();
                    boardView.setiShot(true);
                    // board.setHitArray(666);
                    // boardView.setCoordinates(player.battleship.getBattleshipCoordinates());
                    boardView.invalidate(); // calls ondraw method eventually
                }
                if (player.battleship.isSunk()) { // When you sink the boat
                    Log.w("Abort! Boat has sunk", "Ka-baam");
                    toast("SUNK BATTLESHIP");
                    boardView.setiShot(true);
                    makeLouderExplosion();
                }
            }
            if (!(String.valueOf(x).equals(leftMost)) && (!(String.valueOf(y).equals(rightMost)))) { // When the user misses
                Log.w("Phew", "That was close");
                toast(("Missed"));
                missedSound();
                boardView.setiShot(false); // shot missed
            }
        }
        player.battleship.getXandY();
    }

    /**
     * Makes a swish noise when the player misses a shot.
     */
    private void missedSound() {
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
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
