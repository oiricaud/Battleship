package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.content.Intent;

/**
 * Created by oscarricaud on 3/19/17.
 */

public class GameModel {

    private String difficulty;
    private boolean gameStatus;
    private boolean playerReady;
    private boolean[][] map = new boolean[10][10]; // size of the grid

    public GameModel() {
    }

    public boolean areYouPlaying() {
        return gameStatus;
    }

    public void areYouPlaying(boolean status) {
        this.gameStatus = status;
    }

    public boolean isPlayerReady() {
        return playerReady;
    }

    public void setPlayerReady(boolean playerReady) {
        this.playerReady = playerReady;
    }

    public boolean[][] getAll_X_Y() {
        return map;
    }

    public void putX_Y_On_Map(int x, int y) {
        map[x][y] = true;
    }

    /**
     * @return this returns the difficulty the user chose, which is later retrieved to be displayed
     * on the android phone.
     */
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void updateModel(Context context, String viewWeWantToLaunch) {
        Intent intent = new Intent(context, GameView.class);
        intent.putExtra("viewWeWantToLaunch", viewWeWantToLaunch);
        context.startActivity(intent);
    }
}
