package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by oscarricaud on 3/11/17.
 */

public class HomeActivity extends AppCompatActivity {
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Change font
        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip);
        //Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/eightbit.TTF");
        battleshipLabel.setTypeface(typeface);

        start();
    }

    private void start() {
        playMusic();
        // Start a new game
        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });
    }
    private void playMusic() {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.alterego);
        mp.start();
    }
}
