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
    /**
     * Makes a swish noise when the player misses a shot.
     * @param context
     */
    public void makeMissedSound(Context context) {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create((Context) context, R.raw.missed);
        mp.start();
    }


    /**
     * Makes an explosion sound if the user hits a boat.
     * @param context
     */
    public void makeExplosionSound(Context context) {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.torpedo);
        mp.start();
    }

    /**
     * Makes a louder explosion as the latter method.
     * @param context
     */
    public void makeLouderExplosion(Context context) {
        if (mp!=null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.sunk);
        mp.start();
    }

}
