package edu.utep.cs.cs4330.battleship;

import android.util.Log;

/**
 * Created by oscarricaud on 2/10/17.
 */

public class FleetShip {
    private int hits;
    private int size;
    private boolean isSunk;


    int[][] coordinatesForFinalMap = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public int[][] getXandY() {
      //  Log.w("coordinates", Arrays.deepToString(coordinatesForFinalMap));
        return coordinatesForFinalMap;
    }
    public boolean ispositiontaken(int x, int y){ // verifies if the head of ship is taken
        for(int i = 0; i < coordinatesForFinalMap.length; i++){
            for(int j = 0 ; j < coordinatesForFinalMap.length; j++){
                if(i == x && j == y){
                    Log.w("ispositiontaken", "true");
                    return true;
                }
            }
        }
        return false;
    }
    public void setXandY(int x, int y) {
        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                if (column == x && row == y) {
                    Log.w("MAP for all boats X", String.valueOf(x));
                    Log.w("MAP for all boats Y", String.valueOf(y));
                    coordinatesForFinalMap[x][y] = 1;
                }
            }
        }
    }
}
