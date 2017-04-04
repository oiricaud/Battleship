package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.content.Intent;

/**
 * Created by oscarricaud on 3/19/17.
 */

class GameModel {

    private boolean gameStatus;

    GameModel() {
    }

    boolean areYouPlaying() {
        return gameStatus;
    }

    void areYouPlaying(boolean status) {
        this.gameStatus = status;
    }

    void setPlayerReady() {
    }

    /**
     * @return this returns the difficulty the user chose, which is later retrieved to be displayed
     * on the android phone.
     */

    void setDifficulty() {
    }

    void updateModel(Context context, String viewWeWantToLaunch) {
        Intent intent = new Intent(context, GameView.class);
        intent.putExtra("viewWeWantToLaunch", viewWeWantToLaunch);
        context.startActivity(intent);
    }
}
