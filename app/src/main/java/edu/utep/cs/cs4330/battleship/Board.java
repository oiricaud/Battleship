package edu.utep.cs.cs4330.battleship;

/**
 * A game board consisting of <code>size</code> * <code>size</code> places
 * where battleships can be placed. A place of the board is denoted
 * by a pair of 0-based indices (x, y), where x is a column index
 * and y is a row index. A place of the board can be shot at, resulting
 * in either a hit or miss.
 */
class Board {

    /**
     * Size of this board. This board has
     * <code>size*size </code> places.
     */
    private final int size;
    int[][] grid = new int[10][10];
    private int x;
    private int y;
    private int numberOfShipsFloating;
    private String typeOfPlayer;
    private String nameOfPlayer;
    private int numberOfShots;
    private String address;
    BoardView boardView;
    Ship aircraft = new Ship(5, "aircraft");
    Ship battleship = new Ship(4, "battleship");
    Ship destroyer = new Ship(3, "destroyer");
    Ship submarine = new Ship(3, "submarine");
    Ship patrol = new Ship(2, "patrol");

    /**
     * Create a new board of the given size.
     */
    Board(int size, String player) {
        this.size = size;
        if (player.equals("Human")) {
            setTypeOfPlayer("Human");
            defaultSettingsForHumanPlayer();
        }
        if (player.equals("Computer")) {
            setTypeOfPlayer("Computer");
            // Add random coordinates for a specific boat to the players board grid
            defaultSettingsForComputerPlayer();
        }
    }

    private void defaultSettingsForHumanPlayer() {
        aircraft.setPlaced(false);
        battleship.setPlaced(false);
        destroyer.setPlaced(false);
        submarine.setPlaced(false);
        patrol.setPlaced(false);
        setNumberOfShipsFloating(5);
    }

    private void defaultSettingsForComputerPlayer() {

        addBoatToGrid(aircraft.randomCoordinates());
        aircraft.setPlaced(true);

        addBoatToGrid(battleship.randomCoordinates());
        battleship.setPlaced(true);

        addBoatToGrid(destroyer.randomCoordinates());
        destroyer.setPlaced(true);

        addBoatToGrid(submarine.randomCoordinates());
        submarine.setPlaced(true);

        addBoatToGrid(patrol.randomCoordinates());
        patrol.setPlaced(true);
    }

    /**
     * @param coordinates After the user clicks and drags a specific boat image to the grid; the coordinates must be
     *                    saved to the players board map. This is exactly what this method is doing. Each boat is
     *                    associated with a number. The method then adds the specific Boat id to a 2D Matrix Map,
     *                    which is later retrieved on the
     * @see BoardView class to draw the boats on the grid
     * specifically for the user to visually see where the boats are placed.
     */
    int[][] readBoatCoordinates() {
        return grid;
    }

    void addBoatToGrid(int[][] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = 0; j < coordinates.length; j++) {
                if (coordinates[i][j] == 1) { // Aircraft ID
                    grid[i][j] = 1;
                }
                if (coordinates[i][j] == 2) { // Battleship ID
                    grid[i][j] = 2;
                }
                if (coordinates[i][j] == 3) { // Destroyer ID
                    grid[i][j] = 3;
                }
                if (coordinates[i][j] == 4) { // Submarine ID
                    grid[i][j] = 4;
                }
                if (coordinates[i][j] == 5) { // Patrol ID
                    grid[i][j] = 5;
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
                    grid[i][j] = -1;
                }
                if (coordinates[i][j] == 2) { // Remove battleship coordinates
                    grid[i][j] = -2;
                }
                if (coordinates[i][j] == 3) { // Remove destroyer coordinates
                    grid[i][j] = -3;
                }
                if (coordinates[i][j] == 4) { // Remove submarine coordinates
                    grid[i][j] = -4;
                }
                if (coordinates[i][j] == 5) { // Remove patrol coordinates
                    grid[i][j] = -5;
                }
            }
        }
    }

    /**
     * Return the size of this board.
     */
    int size() {
        return size;
    }

    String getTypeOfPlayer() {
        return typeOfPlayer;
    }

    private void setTypeOfPlayer(String typeOfPlayer) {
        this.typeOfPlayer = typeOfPlayer;
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

    public int getNumberOfShipsFloating() {
        return numberOfShipsFloating;
    }

    public void setNumberOfShipsFloating(int numberOfShipsFloating) {
        this.numberOfShipsFloating = numberOfShipsFloating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Checks if all boats have been placed for this specific board, if so it returns true and allows player
     * to advance to the next activity.
     */
    public boolean playerPlacedAllBoats() {
        return aircraft.isPlaced() && battleship.isPlaced() && destroyer.isPlaced() && submarine.isPlaced() && patrol.isPlaced();
    }

    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    public void setNameOfPlayer(String nameOfPlayer) {
        this.nameOfPlayer = nameOfPlayer;
    }
}
