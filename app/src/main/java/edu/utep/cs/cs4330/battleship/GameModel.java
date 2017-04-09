package edu.utep.cs.cs4330.battleship;

/**
 *
 * Created by oscarricaud on 3/19/17.
 */

class GameModel {

    Player humanPlayer = new Player("Human");
    Player computerPlayer = new Player("Computer");

    private String difficulty;

    GameModel() {

    }


    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
