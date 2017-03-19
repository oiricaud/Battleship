package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

/**
 * Created by oscarricaud on 3/18/17.
 */

public class Music {
    private MediaPlayer mp;
    public void playMusic(Context context) {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        // Play one of these random songs in the background.
        Random random = new Random();
        int obtainRandomNumber = random.nextInt(3 - 1 + 1) + 1;
        if(obtainRandomNumber == 1){
            mp = MediaPlayer.create(context, R.raw.alterego);
        }
        if(obtainRandomNumber == 2){
            mp = MediaPlayer.create(context, R.raw.oblivion);
        }
        if(obtainRandomNumber == 3){
            mp = MediaPlayer.create(context, R.raw.yolo);
        }
        mp.start();
    }

}
