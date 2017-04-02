package edu.utep.cs.cs4330.battleship;

/**
 * Created by oscarricaud on 3/31/17.
 *
 */
class Player {
    private String typeOfPlayer;
    private Board humanBoard = new Board(10);

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
