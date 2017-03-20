package edu.utep.cs.cs4330.battleship;

/**
 * Created by oscarricaud on 3/19/17.
 */

public class GameModel {
    private String difficulty;
    private boolean computerStatus;
    private boolean userStatus;

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
    /* End Setters and Getters */
}
