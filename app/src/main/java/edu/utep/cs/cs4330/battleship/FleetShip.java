package edu.utep.cs.cs4330.battleship;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by oscarricaud on 2/10/17.
 */

public class FleetShip {
    private int hits;
    private int size;
    private boolean isSunk;
    private int[][] getboatsCoordinates;

    int[][] boatsCoordinates = new int[][]{
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };
    public int[][] getboatsCoordinates(){
       //Log.w("Battleship Position:", Arrays.deepToString(boatsCoordinates));
        return boatsCoordinates;
    }
}
