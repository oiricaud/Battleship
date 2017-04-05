package edu.utep.cs.cs4330.battleship;

/*
 * Created by oscarricaud on 2/14/17.
 */
public class Ship {
    int[][] map = new int[10][10]; // size of the grid
    private int size;
    private int hit;
    private boolean isPlaced;
    private String nameOfShip;

    Ship(int size, String ship, String typeOfUser) {
        if (typeOfUser != null) {
            if (typeOfUser.equals("Computer")) {
                setSize(size);
                computerSetCoordinates();
                setSink(false);
            }

            if (typeOfUser.equals("Human")) {
                setSize(size);
                setSink(false);
            }
            nameShip(ship);
        }
    }

    void clearCoordinates() {
        // Clear all elements since the user wants to place boat elsewhere
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] = -1;
            }
        }
    }

    private void computerSetCoordinates() {
        int coordinatesRange = (map.length - getSizeOfBoat());
        int randomX = (int) (Math.random() * coordinatesRange);
        int randomY = (int) (Math.random() * coordinatesRange);
        int direction = (int) (Math.random() * 2);
        if (direction == 1) { // if boat is horizontal
            setPosition();
            for (int i = 0; i < getSizeOfBoat(); i++) { // place boat horizontal
                map[randomX][randomY + i] = 1; // Adding to the right of the head
            }
        } else {
            setPosition();
            for (int j = 0; j < getSizeOfBoat(); j++) { // place boat vertical
                map[randomX + j][randomY] = 1; // Adding below of the head
            }
        }
    }

    int[][] gethumanSetCoordinates() {
        return map;
    }

    int[][] getComputerCoordinates() {
        return map;
    }

    private int getSizeOfBoat() {
        return size;
    }

    private void setSize(int size) {
        this.size = size;
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

    public String getNameOfShip() {
        return nameOfShip;
    }

    public void nameShip(String nameOfShip) {
        this.nameOfShip = nameOfShip;
    }
}
