package edu.utep.cs.cs4330.battleship;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.Arrays;

/**
 *
 * Created by oscarricaud on 3/19/17.
 */

class Game {

    private Board player1Board;
    private Board player2Board;
    private String difficulty;
    private boolean isGameOver;
    private String typeOfGame = "1 VS PC"; // 1 VS 1 OR 1 VS PC
    private int musicTimer = 0;

    Game() {
        if (typeOfGame.equals("1 VS PC")) {
            Board playerBoard = new Board(10, "Human");
            Board computerBoard = new Board(10, "Computer");
            setPlayer1Board(playerBoard);
            setPlayer2Board(computerBoard);
        }
        if (typeOfGame.equals("1 VS 1")) {
            Board playerBoard1 = new Board(10, "Human");
            Board playerBoard2 = new Board(10, "Human");
            setPlayer1Board(playerBoard1);
            setPlayer2Board(playerBoard2);
        }
        setGameOver(false);
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

    /**
     * @return true if it finds a boat at given coordinates
     * false if it does not find a boat at given coordinates
     */
    boolean shootsAt(Board opponentsBoard, int x, int y) {
        Log.w("Opponents board", Arrays.deepToString(opponentsBoard.grid));
        Log.w("X", String.valueOf(x));
        Log.w("Y", String.valueOf(y));
        if (opponentsBoard.grid[x][y] >= 1) {
            int boatID = opponentsBoard.grid[x][y];

            switch (boatID) {
                case 1:  // Shot aircraft
                    opponentsBoard.aircraft.hit();
                    opponentsBoard.addBoatToGrid(opponentsBoard.aircraft.map);
                    opponentsBoard.aircraft.addCoordinates(x, y);
                    opponentsBoard.boardView.gameCoordinates[x][y] = 8; // Set it to 8 to indicate it is a hit
                    getPlayer1Board().shoots(); // Increment counter for # of shots
                    opponentsBoard.boardView.invalidate();
                    return true;

                case 2:  // Shot battleship
                    opponentsBoard.battleship.hit();
                    opponentsBoard.addBoatToGrid(opponentsBoard.battleship.map);
                    opponentsBoard.battleship.addCoordinates(x, y);
                    opponentsBoard.boardView.gameCoordinates[x][y] = 8; // Set it to 8 to indicate it is a hit
                    getPlayer1Board().shoots(); // Increment counter for # of shots
                    opponentsBoard.boardView.invalidate();
                    return true;

                case 3:  // Shot destroyer
                    opponentsBoard.destroyer.hit();
                    opponentsBoard.addBoatToGrid(opponentsBoard.destroyer.map);
                    opponentsBoard.destroyer.addCoordinates(x, y);
                    opponentsBoard.boardView.gameCoordinates[x][y] = 8; // Set it to 8 to indicate it is a hit
                    getPlayer1Board().shoots(); // Increment counter for # of shots
                    opponentsBoard.boardView.invalidate();
                    return true;

                case 4:  // Shot submarine
                    opponentsBoard.submarine.hit();
                    opponentsBoard.addBoatToGrid(opponentsBoard.submarine.map);
                    opponentsBoard.submarine.addCoordinates(x, y);
                    opponentsBoard.boardView.gameCoordinates[x][y] = 8; // Set it to 8 to indicate it is a hit
                    getPlayer1Board().shoots(); // Increment counter for # of shots
                    opponentsBoard.boardView.invalidate();
                    return true;

                case 5:  // Shot patrol
                    opponentsBoard.patrol.hit();
                    opponentsBoard.addBoatToGrid(opponentsBoard.patrol.map);
                    opponentsBoard.patrol.addCoordinates(x, y);
                    opponentsBoard.boardView.gameCoordinates[x][y] = 8; // Set it to 8 to indicate it is a hit
                    getPlayer1Board().shoots(); // Increment counter for # of shots
                    opponentsBoard.boardView.invalidate();
                    return true;
            }
        }
        opponentsBoard.boardView.gameCoordinates[x][y] = -9; // Set it to -9 to indicate it is a miss
        opponentsBoard.boardView.invalidate();
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

    boolean hasThisBoatBeenPlaced(Ship boat) {
        return boat.isPlaced();
    }

    int getMusicTimer() {
        return musicTimer;
    }

    void setMusicTimer(int musicTimer) {
        this.musicTimer = musicTimer;
    }

    public Board getPlayer1Board() {
        return player1Board;
    }

    public void setPlayer1Board(Board player1Board) {
        this.player1Board = player1Board;
    }

    public Board getPlayer2Board() {
        return player2Board;
    }

    public void setPlayer2Board(Board player2Board) {
        this.player2Board = player2Board;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
