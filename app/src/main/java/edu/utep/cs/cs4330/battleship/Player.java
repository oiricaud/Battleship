package edu.utep.cs.cs4330.battleship;

import android.util.Log;

/*
 * Created by oscarricaud on 3/31/17.
 */
class Player {
    int[][] boardGrid = new int[10][10];
    private String typeOfPlayer;
    private int numberOfShots;
    private Board playerBoard = new Board(10);

    Ship aircraft = new Ship(5, "aircraft");
    Ship battleship = new Ship(4, "battleship");
    Ship destroyer = new Ship(3, "destroyer");
    Ship submarine = new Ship(3, "submarine");
    Ship patrol = new Ship(2, "patrol");

    /**
     * @param player
     * @param board
     */
    Player(String player, Board board) {
        if (player.equals("Human")) {
            setTypeOfPlayer("Human");
        }
        if (player.equals("Computer")) {
            setTypeOfPlayer("Computer");
            Log.w("2Here", "here");
            // Add random coordinates for a specific boat to the players board grid
            addCoordinates(aircraft.map);
            addCoordinates(battleship.map);
            addCoordinates(destroyer.map);
            addCoordinates(submarine.map);
            addCoordinates(patrol.map);
        }
        aircraft.setPlaced(false);
        battleship.setPlaced(false);
        destroyer.setPlaced(false);
        submarine.setPlaced(false);
        patrol.setPlaced(false);

        setPlayerBoard(board);
    }

    /**
     * @param coordinates After the user clicks and drags a specific boat image to the grid; the coordinates must be
     *                    saved to the players board map. This is exactly what this method is doing. Each boat is
     *                    associated with a number. The method then adds the specific Boat id to a 2D Matrix Map,
     *                    which is later retrieved on the
     * @see BoardView class to draw the boats on the grid
     * specifically for the user to visually see where the boats are placed.
     */
    void addCoordinates(int[][] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates.length; j++) {
                if (coordinates[i][j] == 1) { // Aircraft ID
                    boardGrid[i][j] = 1;
                }
                if (coordinates[i][j] == 2) { // Battleship ID
                    boardGrid[i][j] = 2;
                }
                if (coordinates[i][j] == 3) { // Destroyer ID
                    boardGrid[i][j] = 3;
                }
                if (coordinates[i][j] == 4) { // Submarine ID
                    boardGrid[i][j] = 4;
                }
                if (coordinates[i][j] == 5) { // Patrol ID
                    boardGrid[i][j] = 5;
                }
            }
        }
    }

    /**
     * @param coordinates Are the coordinates of a specific boat, when the user places a boat and decides to place
     *                    the boat elsewhere we must delete the coordinates of the previous location and update the
     * @see BoardView class to update the onDraw method.
     */
    void removeCoordinates(int[][] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates.length; j++) {
                if (coordinates[i][j] == 1) { // Remove aircraft coordinates
                    boardGrid[i][j] = -1;
                }
                if (coordinates[i][j] == 2) { // Remove aircraft coordinates
                    boardGrid[i][j] = -2;
                }
                if (coordinates[i][j] == 3) { // Remove destroyer coordinates
                    boardGrid[i][j] = -3;
                }
                if (coordinates[i][j] == 4) { // Remove submarine coordinates
                    boardGrid[i][j] = -4;
                }
                if (coordinates[i][j] == 5) { // Remove patrol coordinates
                    boardGrid[i][j] = -5;
                }
            }
        }
    }

    String getTypeOfPlayer() {
        return typeOfPlayer;
    }

    private void setTypeOfPlayer(String typeOfPlayer) {
        this.typeOfPlayer = typeOfPlayer;
    }

    Board getPlayerBoard() {
        return playerBoard;
    }

    private void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * @return the number of shots the user has shot at boats
     */
    public int getNumberOfShots() {
        return numberOfShots;
    }
    /**
     * @param countShots set the count of shots each time the user fires.
     */
    void shoots() {
        this.numberOfShots = 1 + numberOfShots;
    }
}
