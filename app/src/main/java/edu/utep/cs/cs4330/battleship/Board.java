package edu.utep.cs.cs4330.battleship;

        import android.graphics.Canvas;
        import android.util.Log;

        import java.util.LinkedList;

/**
 * A game board consisting of <code>size</code> * <code>size</code> places
 * where battleships can be placed. A place of the board is denoted
 * by a pair of 0-based indices (x, y), where x is a column index
 * and y is a row index. A place of the board can be shot at, resulting
 * in either a hit or miss.
 */
public class Board {
    private BoardView boardView;
    public int x;
    public int y;

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }
    public String coordinatesConvertToString() {
        return "(" + x + "," + y + ")";
    }
    /**
     * Size of this board. This board has
     * <code>size*size </code> places.
     */
    private final int size;

    /** Create a new board of the given size. */
    public Board(int size) {
        this.size = size;
    }

    /** Return the size of this board. */
    public int size() {
        return size;
    }

    // Suggestions:
    // 1. Consider using the Observer design pattern so that a client,
    //    say a BoardView, can observe changes on a board, e.g.,
    //    hitting a place, sinking a ship, and game over.

    // 2. Introduce methods including the following:
    //    public boolean placeShip(Ship ship, int x, int y, boolean dir)
    //    public void hit(Place place)
    //    public Place at(int x, int y)
    //    public Place[] places()
    //    public int numOfShots()
    //    public boolean isGameOver()
    //    ...
    public boolean placeShip(Ship ship, int x, int y, boolean dir) {
        return false;
    }
    public void hit(Place place){
    }
    public Place at(int x, int y, BoardView boardView)
    {
        setCoordinates(x, y);
        return null;
    }
    public Place[] places(){
        return new Place[0];
    }
    public int numOfShots(){
        return 0;
    }
    public boolean isGameOver(){
        return true;
    }
}
