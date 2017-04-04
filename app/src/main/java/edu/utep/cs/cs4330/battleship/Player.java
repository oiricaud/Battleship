package edu.utep.cs.cs4330.battleship;

/**
 * Created by oscarricaud on 3/31/17.
 *
 */
class Player {
    private String typeOfPlayer;
    private Board humanBoard = new Board(10);
    public int[][] map = new int[10][10]; // size of the grid
    Ship aircraft = new Ship(5, "aircraft", getTypeOfPlayer());
    Ship battleship = new Ship(4, "battleship", getTypeOfPlayer());
    Ship destroyer = new Ship(3, "destroyer", getTypeOfPlayer());
    Ship submarine = new Ship(3, "submarine", getTypeOfPlayer());
    Ship patrol = new Ship(2, "patrol", getTypeOfPlayer());

    Player(String player, Board board){
        if(player.equals("human")){
            setTypeOfPlayer("human");
            setHumanBoard(board);
        }
    }
    public void addCoordinates(int[][] coordinates){
        for(int i = 0 ; i < coordinates.length; i++){
            for(int j = 0 ; j < coordinates.length; j++){
                if(coordinates[i][j] == 1){ // Aircraft
                    map[i][j] = 1;
                }
                if(coordinates[i][j] == 2){ // Battleship
                    map[i][j] = 2;
                }
            }
        }
    }
    public void removeCoordinates(int[][] coordinates, String typeOfShip){
        for(int i = 0 ; i < coordinates.length; i++){
            for(int j = 0 ; j < coordinates.length; j++){
                if(typeOfShip.equals("aircraft") && coordinates[i][j] == 1){ // Remove aircraft coordinates
                    map[i][j] = -1;
                }
                if(typeOfShip.equals("battleship") && coordinates[i][j] == 2){ // Remove aircraft coordinates
                    map[i][j] = -2;
                }
            }
        }
    }
    public String getTypeOfPlayer() {
        return typeOfPlayer;
    }

    public void setTypeOfPlayer(String typeOfPlayer) {
        this.typeOfPlayer = typeOfPlayer;
    }

    public Board getHumanBoard() {
        return humanBoard;
    }

    public void setHumanBoard(Board humanBoard) {
        this.humanBoard = humanBoard;
    }

}
