package edu.utep.cs.cs4330.battleship;

import android.util.Log;

/**
 * Created by oscarricaud on 2/10/17.
 */

public class Player {
    Battleship battleship = new Battleship();
    public void setUpBoats() {
        battleship.placeShip(10); // place random places based on the board size

    }
}
