package edu.utep.cs.cs4330.battleship;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

public class Battleship extends FleetShip {
    private int battleshipSize = 4;
    private int numOfHits = 4;
    private boolean hasCoordinates = false;
    private int[] coordinates = new int[2];
    private boolean sunk;
    private int axis = 0; // 0 = horizontal, 1 = vertical
    private ArrayList<String> battleshipFinalCoordinates = new ArrayList<String>();
    private ArrayList<String> whereisBattleship;
    private LinkedList<String> placesthathavebeenhit = new LinkedList<>();

    public void placeShip(int boardsize) {
        int[] coorRandom = getRandomCoordinates(boardsize);
        coordinates[0] = coorRandom[0];
        coordinates[1] = coorRandom[1];
    }

    private int[] getRandomCoordinates(int boardsize) {
        int [] coordinates = new int[2];
                if(!hasCoordinates) { // By default, it does not have any coordinates.

                    // Set bounds for the random values we will be obtaining later.
                    int axisBound = 2;
                    int coordinatesRange = (boardsize - battleshipSize);

                    // Set at random a position of the battleship which can be parallel to the x-axis or y-axis.
                    axis = (int) (Math.random()*axisBound);
                    coordinates[0] = (int) (Math.random() * coordinatesRange);
                    coordinates[1] = (int) (Math.random() * coordinatesRange);

                    // Debugging
                    Log.w(" (x,y)", String.valueOf(coordinates[0]) + ", " + String.valueOf(coordinates[1]));

                    for(int i = 0 ; i < battleshipSize; i++){
                        // if axis of boat is vertical
                        if(axis == 0) {
                       //     Log.w(" Position is vertical", String.valueOf(axis));
                            battleshipFinalCoordinates.add(i, coordinates[0] + " " + coordinates[1]);
                            coordinates[1]++;
                        }
                        // if axis of boat is horizontal
                        if(axis == 1) {
                      //      Log.w(" Position is horizontal", String.valueOf(axis));
                            battleshipFinalCoordinates.add(i, coordinates[0] + " " + coordinates[1]);
                            coordinates[0]++;
                        }
                    }
                    Log.w("Battleship Position:", String.valueOf(battleshipFinalCoordinates));
                    setBattleshipPosition(battleshipFinalCoordinates);
                    hasCoordinates = true;
                }
        return coordinates;
    }

    public ArrayList<String> getBattleshipCoordinates() {
        return whereisBattleship;
    }

    public void setBattleshipPosition(ArrayList<String> whereisBattleship) {
       // getboatsCoordinates();
        this.whereisBattleship = whereisBattleship;
    }

    public int getNumOfHits() {
        Log.w("Num of hits left", String.valueOf(numOfHits));
        return this.numOfHits;
    }

    public void hit() {
        numOfHits = numOfHits - 1;
    }

    public boolean isSunk() {
        return this.sunk;
    }

    public void setSunk() {
        this.sunk = true;
    }
}
