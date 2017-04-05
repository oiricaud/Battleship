package edu.utep.cs.cs4330.battleship;

/*
 * Created by oscarricaud on 3/31/17.
 */
class Player {
    private String typeOfPlayer;
    private Board playerBoard = new Board(10);
    int[][] boardGrid = new int[10][10];

    Ship aircraft = new Ship(5, "aircraft", getTypeOfPlayer());
    Ship battleship = new Ship(4, "battleship", getTypeOfPlayer());
    Ship destroyer = new Ship(3, "destroyer", getTypeOfPlayer());
    Ship submarine = new Ship(3, "submarine", getTypeOfPlayer());
    Ship patrol = new Ship(2, "patrol", getTypeOfPlayer());

    /**
     * @param player
     * @param board
     */
    Player(String player, Board board) {
        if (player.equals("human")) {
            setTypeOfPlayer("human");
        }
        if(player.equals("computer")){
            setTypeOfPlayer("computer");
        }
        setPlayerBoard(board);
    }

    /**
     * @param coordinates After the user clicks and drags a specific boat image to the grid; the coordinates must be
     *                    saved to the players board map. This is exactly what this method is doing. Each boat is
     *                    associated with a number. The method then adds the specific Boat id to a 2D Matrix Map,
     *                    which is later retrieved on the
     *                    @see BoardView class to draw the boats on the grid
     *                    specifically for the user to visually see where the boats are placed.
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
     *                    @see BoardView class to update the onDraw method.
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

}
