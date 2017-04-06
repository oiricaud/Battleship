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

    /**
     * Create a new board of the given size.
     */
    Board(int size) {
        this.size = size;
    }

    /**
     * @param coordinates After the user clicks and drags a specific boat image to the grid; the coordinates must be
     *                    saved to the players board map. This is exactly what this method is doing. Each boat is
     *                    associated with a number. The method then adds the specific Boat id to a 2D Matrix Map,
     *                    which is later retrieved on the
     * @see BoardView class to draw the boats on the grid
     * specifically for the user to visually see where the boats are placed.
     */
    int[][] readCoordinates() {
        return grid;
    }

    void addCoordinates(int[][] coordinates) {
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

}
