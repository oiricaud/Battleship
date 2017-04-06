package edu.utep.cs.cs4330.battleship;

/*
 * Created by oscarricaud on 2/14/17.
 */
public class Ship {
    int[][] map = new int[10][10]; // size of the grid
    private int size;
    private int hit;
    private boolean isPlaced;
    private boolean shootShip;
    private String nameOfShip;

    Ship(int size, String ship) {
        setSize(size);
        setSink(false);
        nameShip(ship);
        randomCoordinates();
    }

    void clearCoordinates() {
        // Clear all elements since the user wants to place boat elsewhere
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] = -1;
            }
        }
    }

    int[][] coordinates() {

        return map;
    }

    private void randomCoordinates() {
        int coordinatesRange = (map.length - getSizeOfBoat());
        int randomX = (int) (Math.random() * coordinatesRange);
        int randomY = (int) (Math.random() * coordinatesRange);
        int direction = (int) (Math.random() * 2);

        if (direction == 1) { // if boat is horizontal
            setPosition();
            for (int i = 0; i < getSizeOfBoat(); i++) { // place boat horizontal
                if(getNameOfShip().equals("aircraft")){
                    map[randomX][randomY + i] = 1; // Adding to the right of the head
                }
                if(getNameOfShip().equals("battleship")){
                    map[randomX][randomY + i] = 2; // Adding to the right of the head
                }
                if(getNameOfShip().equals("destroyer")){
                    map[randomX][randomY + i] = 3; // Adding to the right of the head
                }
                if(getNameOfShip().equals("submarine")){
                    map[randomX][randomY + i] = 4; // Adding to the right of the head
                }
                if(getNameOfShip().equals("patrol")){
                    map[randomX][randomY + i] = 5; // Adding to the right of the head
                }
            }
        } else {
            setPosition();
            for (int j = 0; j < getSizeOfBoat(); j++) { // place boat vertical
                if(getNameOfShip().equals("aircraft")){
                    map[randomX + j][randomY] = 1; // Adding below of the head
                }
                if(getNameOfShip().equals("battleship")){
                    map[randomX + j][randomY] = 2; // Adding below of the head
                }
                if(getNameOfShip().equals("destroyer")){
                    map[randomX + j][randomY] = 3; // Adding below of the head
                }
                if(getNameOfShip().equals("submarine")){
                    map[randomX + j][randomY] = 4; // Adding below of the head
                }
                if(getNameOfShip().equals("patrol")){
                    map[randomX + j][randomY] = 5; // Adding below of the head
                }
            }
        }
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

    private String getNameOfShip() {
        return nameOfShip;
    }

    private void nameShip(String nameOfShip) {
        this.nameOfShip = nameOfShip;
    }

    public void addCoordinates(int x, int y) {
        if (getNameOfShip().equals("aircraft")) {
            map[x][y] = 1;
        }
    }
}
