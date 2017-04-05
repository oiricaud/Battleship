package edu.utep.cs.cs4330.battleship;

/*
 * Created by oscarricaud on 3/31/17.
 */
class Player {
    Board gameBoard = new Board(10);
    Ship aircraft = new Ship(5, "aircraft");
    Ship battleship = new Ship(4, "battleship");
    Ship destroyer = new Ship(3, "destroyer");
    Ship submarine = new Ship(3, "submarine");
    Ship patrol = new Ship(2, "patrol");
    private String typeOfPlayer;
    private int numberOfShots;
    private Board playerBoard = new Board(10);

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
            // Add random coordinates for a specific boat to the players board grid
            board.addCoordinates(aircraft.map);
            board.addCoordinates(battleship.map);
            board.addCoordinates(destroyer.map);
            board.addCoordinates(submarine.map);
            board.addCoordinates(patrol.map);
        }
        aircraft.setPlaced(false);
        battleship.setPlaced(false);
        destroyer.setPlaced(false);
        submarine.setPlaced(false);
        patrol.setPlaced(false);

        setPlayerBoard(board);
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
    boolean shootAtBoard(int x, int y) {
        if (gameBoard.grid[x][y] >= 1) {
            int boatID = gameBoard.grid[x][y];
            if (boatID == 1) { // Shot aircraft
                aircraft.hit();
                return true;
            }
            if (boatID == 2) { // Shot battleship
                battleship.hit();
                return true;
            }
            if (boatID == 3) { // Shot destroyer
                destroyer.hit();
                return true;
            }
            if (boatID == 4) { // Shot submarine
                submarine.hit();
                return true;
            }
            if (boatID == 5) { // Shot patrol
                patrol.hit();
                return true;
            }
        }
        return false;
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
