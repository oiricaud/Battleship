package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 *
 * Created by oscarricaud on 3/19/17.
 */

class GameModel {
    /* Begin Fields for Human */
    Player humanPlayer = new Player("Human");
    /* Begin Fields for AI */
    Player computerPlayer = new Player("Computer");
    /* End Fields for Human */
    private MediaPlayer music; // For music background
    /* End Fields for AI */
    private String difficulty;

    GameModel() {

    }
/*
    void playMusic(Context context) {
        if (music != null) {
            music.stop();
            music.reset();
            music.release();
        }
        else {
            // Play one of these random songs in the background.
            Random random = new Random();
            int obtainRandomNumber = random.nextInt(4);
            if (obtainRandomNumber == 1) {
                music = MediaPlayer.create(context, R.raw.alterego);
            }
            if (obtainRandomNumber == 2) {
                music = MediaPlayer.create(context, R.raw.oblivion);
            }
            if (obtainRandomNumber == 3) {
                music = MediaPlayer.create(context, R.raw.yolo);
            }
            music.start();
        }
    }
    */
    /**
     * @return this returns the difficulty the user chose, which is later retrieved to be displayed
     * on the android phone.
     */

    void updateModel(Context context, String viewWeWantToLaunch) {
        Intent intent = new Intent(context, GameController.class);
        intent.putExtra("viewWeWantToLaunch", viewWeWantToLaunch);
        context.startActivity(intent);
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
