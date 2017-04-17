package edu.utep.cs.cs4330.battleship;

import android.media.MediaPlayer;
import android.util.Log;

/**
 *
 * Created by oscarricaud on 3/19/17.
 */

class Game {

    private Player humanPlayer;
    private Player humanPlayer2;
    private Player computerPlayer;
    private String difficulty;
    private boolean gameStatus;
    private String typeOfGame = "1 VS PC"; // 1 VS 1 OR 1 VS PC
    private int musicTimer = 0;

    Game() {
        if (typeOfGame.equals("1 VS PC")) {
            humanPlayer = new Player("Human");
            computerPlayer = new Player("Computer");
            setHumanPlayer(humanPlayer);
            setComputerPlayer(computerPlayer);
            gameStatus = true;
        }
        if (typeOfGame.equals("1 VS 1")) {
            humanPlayer = new Player("Human");
            humanPlayer2 = new Player("Human");
            setHumanPlayer(humanPlayer);
            setHumanPlayer2(humanPlayer2);
            gameStatus = true;
        }
    }

    static boolean isMediaPlaying(MediaPlayer music) {
        if (music != null) {
            try {
                return music.isPlaying();
            } catch (Exception e) {
                Log.w("Wut", "Check isMediaPlaying(), there might be an issue");
            }
        }
        return false;
    }


    public String getTypeOfGame() {
        return typeOfGame;
    }

    void setTypeOfGame(String typeOfGame) {
        this.typeOfGame = typeOfGame;
    }

    public String getDifficulty() {
        return difficulty;
    }

    void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getGameStatus() {
        return gameStatus;
    }

    public void changeGameStatus(boolean gameStarted) {
        this.gameStatus = gameStarted;
    }

    Player getHumanPlayer() {
        return humanPlayer;
    }

    private void setHumanPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    Player getComputerPlayer() {
        return computerPlayer;
    }

    private void setComputerPlayer(Player computerPlayer) {
        this.computerPlayer = computerPlayer;
    }

    boolean hasThisBoatBeenPlaced(Ship boat) {
        return boat.isPlaced();
    }

    int getMusicTimer() {
        return musicTimer;
    }

    void setMusicTimer(int musicTimer) {
        this.musicTimer = musicTimer;
    }

    Player getHumanPlayer2() {
        return humanPlayer2;
    }

    private void setHumanPlayer2(Player humanPlayer2) {
        this.humanPlayer2 = humanPlayer2;
    }
}
