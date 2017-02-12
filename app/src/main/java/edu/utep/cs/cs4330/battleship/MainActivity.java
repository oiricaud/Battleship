package edu.utep.cs.cs4330.battleship;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Battleship!!!
 */
public class MainActivity extends AppCompatActivity {
    private Board board;
    private BoardView boardView;
    private PopupWindow pw;
    private int countNumOfHits = 0;

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
                toast(String.format("Touched: %d, %d", x, y));

            }
        });
        Button newButton = (Button) findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
        TextView counter =(TextView) findViewById(R.id.countOfHits);
            @Override
            public void onClick(View view) {
                final Player player = new Player();
                player.setUpBoats();
                boardView.addBoardTouchListener(new BoardView.BoardTouchListener() {
                    @Override
                    public void onTouch(int x, int y) {
                        countNumOfHits++;
                        counter.setText(String.valueOf("Number of Shots:" + countNumOfHits));
                        toast(String.format("Touched: %d, %d", x, y));
                        for(int i = 0; i < player.battleship.getBattleshipCoordinates().size(); i++){
                            // (LEFTMOST, RIGHTMOST), (LEFTMOST+1, RIGHTMOST+1)... (LEFTMOST+N, RIGHTMOST+N)
                            String leftMost = String.valueOf(player.battleship.getBattleshipCoordinates().get(i).charAt(0));
                            String rightMost = String.valueOf(player.battleship.getBattleshipCoordinates().get(i).charAt(2));
                            if((String.valueOf(x).equals(leftMost)) && (String.valueOf(y).equals(rightMost))) {
                                Log.w("Critical hit! X:", String.valueOf(x));
                                Log.w("Critical hit! Y:", String.valueOf(y));
                                makeExplosionSound();
                            }
                            else{
                                Log.w("Missed!", "");
                            }
                        }
                    }
                });
            }
        });
    }

    private void makeExplosionSound() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.torpedo);
        Log.v("Kill the noise", "Ka-baam");
        mp.start();
    }

    /** Show a toast message. */
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
