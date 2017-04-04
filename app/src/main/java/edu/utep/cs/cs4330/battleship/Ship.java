package edu.utep.cs.cs4330.battleship;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by oscarricaud on 2/14/17.
 */
public class Ship {
    public int[][] map = new int[10][10]; // size of the grid
    private Map<Integer, Integer> mapOfBoat = new HashMap<>();
    private String typeOfPlayer;
    private int size;
    private String name;
    private int hit;
    private boolean isPlaced;
    private LinkedList<Integer> xShipCoordinate = new LinkedList<>();
    private LinkedList<Integer> yShipCoordinate = new LinkedList<>();

    Ship(int size, String nameofship, String typeOfUser) {
        if (typeOfUser != null) {
            if (typeOfUser.equals("Computer")) {
                setSize(size);
                setName(nameofship);
                setTypeOfPlayer("computer");
                computerSetCoordinates();
                setSink(false);
            }

            if (typeOfUser.equals("Human")) {
                setSize(size);
                setName(nameofship);
                setTypeOfPlayer("human");
                setSink(false);
            }
        }
    }

    int[][] clearCoordinates() {
        // Clear all elements since the user wants to place boat elsewhere
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] = -1;
            }
        }
        return map;
    }

    void humanSetCoordinates(int size, int x, int y) {
        if (map != null) {
            // Boat has not been placed, therefore save user coordinates
            for (int i = 0; i < size; i++) {
                map[i + x][y] = 1;
            }
        }
    }

    private void computerSetCoordinates() {
        int coordinatesRange = (map.length - getSize());
        int randomX = (int) (Math.random() * coordinatesRange);
        int randomY = (int) (Math.random() * coordinatesRange);
        int direction = (int) (Math.random() * 2);
        if (direction == 1) { // if boat is horizontal
            setPosition();
            for (int i = 0; i < getSize(); i++) { // place boat horizontal
                map[randomX][randomY + i] = 1; // Adding to the right of the head
            }
        } else {
            setPosition();
            for (int j = 0; j < getSize(); j++) { // place boat vertical
                map[randomX + j][randomY] = 1; // Adding below of the head
            }
        }
    }

    int[][] gethumanSetCoordinates() {
        return map;
    }

    int[][] getComputerCordinates() {
        return map;
    }

    int getSize() {
        return size;
    }

    private void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setPosition() {
    }

    private void setSink(boolean sink) {
    }

    int getHit() {
        return hit;
    }

    void hit() {
        hit++;
    }

    boolean isPlaced() {
        return isPlaced;
    }

    void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    LinkedList<Integer> getX() {
        return xShipCoordinate;
    }

    void setX(int x) {
        xShipCoordinate.add(x);
    }

    LinkedList<Integer> getY() {
        return yShipCoordinate;
    }

    void setY(int y) {
        yShipCoordinate.add(y);
    }

    String getTypeOfPlayer() {
        return typeOfPlayer;
    }

    private void setTypeOfPlayer(String typeOfPlayer) {
        this.typeOfPlayer = typeOfPlayer;
    }

    public void addCoordinates(int x, int y) {
        for (int i = 0; i < getSize(); i++) {
            if ((x + i) > 10) { // Make sure it is not greater than the width of the board
                int tempForXandY = (x + i) * 10000 + y;
                mapOfBoat.put(i, tempForXandY);
            }
        }
    }

    public void readMeAllCoordinates() {
        // Iterating over values only
        for (int i = 0; i < getSize(); i++) {
            int divideX = mapOfBoat.get(i) / 10000;
            int moduloY = mapOfBoat.get(i) % 10000;
            Log.w("Ship X:", String.valueOf(divideX) + ", Ship Y:" + String.valueOf(moduloY));
        }
    }
}
