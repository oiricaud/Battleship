package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by oscarricaud on 3/11/17.
 */

public class LaunchView extends AppCompatActivity {
    private MediaPlayer mp;
    private Font eightBitFont = new Font("fonts/eightbit.TTF");
    private String difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        start();
    }
    // The beginning to a wonderful journey
    private void start() {
        playMusic();
        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip); // Change font
        eightBitFont.changeFont(this, battleshipLabel);

        // Begin to the next activity, placing boats on the map
        Button startButton = (Button) findViewById(R.id.start);
        eightBitFont.changeFont(this, startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLevel(); // Ask the user what Level of difficulty do they want to play on.
            }
        });
    }
    /** Changes the current view, @see layout/activity_launch to a view where the user can choose
     *  the game play difficulty level, @see  layout/activity_level
     */
    private void chooseLevel() {
        setContentView(R.layout.activity_level);
        Button easy = (Button) findViewById(R.id.easy);
        Button medium = (Button) findViewById(R.id.medium);
        Button hard = (Button) findViewById(R.id.hard);

        // Change font to a cooler 8 bit font.
        eightBitFont.changeFont(this, easy);
        eightBitFont.changeFont(this, medium);
        eightBitFont.changeFont(this, hard);

        easy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               setDifficulty("easy");
               humanPlaceBoats();  // Takes the user to @see Place_Ship to place ships on the grid.
            }
        });
        medium.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("medium");
                humanPlaceBoats(); // Takes the user to @see Place_Ship to place ships on the grid.
            }
        });
        hard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("hard");
                humanPlaceBoats(); // Takes the user to @see Place_Ship to place ships on the grid.
            }
        });
    }

    private void humanPlaceBoats() {
        Intent intent = new Intent(LaunchView.this, Place_Ship.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        LaunchView.this.startActivity(intent);
        /** Fading Transition Effect */
        LaunchView.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void playMusic() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        // Play one of these random songs in the background.
        Random random = new Random();
        int obtainRandomNumber = random.nextInt(3 - 1 + 1) + 1;
        if(obtainRandomNumber == 1){
            mp = MediaPlayer.create(this, R.raw.alterego);
        }
        if(obtainRandomNumber == 2){
            mp = MediaPlayer.create(this, R.raw.oblivion);
        }
        if(obtainRandomNumber == 3){
            mp = MediaPlayer.create(this, R.raw.yolo);
        }
        mp.start();
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
