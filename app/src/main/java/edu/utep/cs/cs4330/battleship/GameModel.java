package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oscarricaud on 3/19/17.
 */

public class GameModel  {
    private String difficulty;
    private boolean computerStatus;
    private boolean userStatus;
    public GameModel(){

    }
    /* Begin Setters and Getters */
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

    public boolean getComputerStatus() {
        return computerStatus;
    }

    public void setComputerStatus(boolean computerReady) {
        computerStatus = computerReady;
    }

    public boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean humanReady) {
        userStatus = humanReady;
    }

    public void updateView(Context context, String viewWeWantToLaunch, String shouldWeStartGame) {
        Intent intent = new Intent(context, GameView.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("shouldWeStartGame", shouldWeStartGame);
        intent.putExtra("viewWeWantToLaunch", viewWeWantToLaunch);
        context.startActivity(intent);
    }
}
