package edu.utep.cs.cs4330.battleship;

import android.util.Log;

import java.util.Arrays;

/*
 * Created by oscarricaud on 3/31/17.
 */
class Player {
    Board gameBoard = new Board(10);
    BoardView boardView;
    Ship aircraft = new Ship(5, "aircraft");
    Ship battleship = new Ship(4, "battleship");
    Ship destroyer = new Ship(3, "destroyer");
    Ship submarine = new Ship(3, "submarine");
    Ship patrol = new Ship(2, "patrol");
    private int numberOfShipsFloating;
    private String typeOfPlayer;
    private int numberOfShots;
    private String address;
    /**
     * @param player
     * @param board
     */
    Player(String player) {
        if (player.equals("Human")) {
            setTypeOfPlayer("Human");

        }
        if (player.equals("Computer")) {
            setTypeOfPlayer("Computer");
            // Add random coordinates for a specific boat to the players board grid
            gameBoard.addCoordinates(aircraft.map);
            gameBoard.addCoordinates(battleship.map);
            gameBoard.addCoordinates(destroyer.map);
            gameBoard.addCoordinates(submarine.map);
            gameBoard.addCoordinates(patrol.map);
        }
        setNumberOfShipsFloating(5);
        aircraft.setPlaced(false);
        battleship.setPlaced(false);
        destroyer.setPlaced(false);
        submarine.setPlaced(false);
        patrol.setPlaced(false);
    }

    String getTypeOfPlayer() {
        return typeOfPlayer;
    }

    private void setTypeOfPlayer(String typeOfPlayer) {
        this.typeOfPlayer = typeOfPlayer;
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
            if (boatID == 1) { // Shot aircraft
                aircraft.hit();
                opponentsBoard.addCoordinates(aircraft.map);
                aircraft.addCoordinates(x, y);
                return true;
            }
            if (boatID == 2) { // Shot battleship
                battleship.hit();
                opponentsBoard.addCoordinates(battleship.map);
                battleship.addCoordinates(x, y);
                return true;
            }
            if (boatID == 3) { // Shot destroyer
                destroyer.hit();
                opponentsBoard.addCoordinates(destroyer.map);
                destroyer.addCoordinates(x, y);
                return true;
            }
            if (boatID == 4) { // Shot submarine
                submarine.hit();
                opponentsBoard.addCoordinates(submarine.map);
                submarine.addCoordinates(x, y);
                return true;
            }
            if (boatID == 5) { // Shot patrol
                patrol.hit();
                opponentsBoard.addCoordinates(patrol.map);
                patrol.addCoordinates(x, y);
                return true;
            }

        }
        return false;
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
}
