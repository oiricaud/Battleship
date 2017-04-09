package edu.utep.cs.cs4330.battleship;

import android.media.MediaPlayer;
import android.util.Log;

/**
 *
 * Created by oscarricaud on 3/19/17.
 */

class GameModel {

    private Player humanPlayer;
    private Player computerPlayer;
    private String difficulty;
    private boolean gameStatus;
    private String typeOfGame = "1 VS PC"; // 1 VS 1 OR 1 VS PC
    private int musicTimer = 0;

    GameModel() {
        if (typeOfGame.equals("1 VS PC")) {
            humanPlayer = new Player("Human");
            computerPlayer = new Player("Computer");
            setHumanPlayer(humanPlayer);
            setComputerPlayer(computerPlayer);
            gameStatus = true;
        }
    }

    public static boolean isMediaPlaying(MediaPlayer music) {
        if (music != null) {
            try {
                return music.isPlaying();
            } catch (Exception e) {
                Log.w("Wut", "Check isMediaPlaying(), there might be an issue");
            }
        }
        return false;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getGameStatus() {
        return gameStatus;
    }

    public void changeGameStatus(boolean gameStarted) {
        this.gameStatus = gameStarted;
    }

    public void setTypeOfGame(String typeOfGame) {
        this.typeOfGame = typeOfGame;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public void setHumanPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    public Player getComputerPlayer() {
        return computerPlayer;
    }

    public void setComputerPlayer(Player computerPlayer) {
        this.computerPlayer = computerPlayer;
    }

    boolean hasThisBoatBeenPlaced(Ship boat) {
        return boat.isPlaced();
    }

    public int getMusicTimer() {
        return musicTimer;
    }

    public void setMusicTimer(int musicTimer) {
        this.musicTimer = musicTimer;
    }
}
