package edu.utep.cs.cs4330.battleship;

import android.util.Log;

import java.util.ArrayList;

public class Battleship extends FleetShip {
    private int battleshipSize = 4;
    private boolean hasCoordinates = false;
    private int[] coordinates = new int[2];
    private int axis = 0; // 0 = horizontal, 1 = vertical
    private int [] battleship = new int [battleshipSize]; // coordinates of the battleship
    private ArrayList<String> battleshipFinalCoordinates = new ArrayList<String>();
    private ArrayList<String> whereisBattleship;

    public void placeShip(int boardsize) {
        int[] coorRandom = getRandomCoordinates(boardsize);
        coordinates[0] = coorRandom[0];
        coordinates[1] = coorRandom[1];
    }

    private int[] getRandomCoordinates(int boardsize) {
        int [] coordinates = new int[2];
                if(!hasCoordinates) { // By default, it does not have any coordinates.

                    // Set bounds for the random values we will be obtaining later.
                    int randPosition = 2;
                    int range = (boardsize - battleshipSize);

                    // Set at random a position of the battleship which will can be parallel to the x-axis or y-axis.
                    axis = (int) (Math.random()*randPosition);
                    coordinates[0] = (int) (Math.random() * range);
                    coordinates[1] = (int) (Math.random() * range);

                    // Debugging
                    Log.w("Battleship range", String.valueOf(range));
                    Log.w(" Has coordinates", String.valueOf(hasCoordinates));
                    Log.w(" x", String.valueOf(coordinates[0]));
                    Log.w(" y", String.valueOf(coordinates[1]));
                    Log.w(" (x,y)", String.valueOf(coordinates[0]) + ", " + String.valueOf(coordinates[1]));

                    for(int i = 0 ; i < battleshipSize; i++){
                        // if axis of boat is vertical
                        if(axis == 0) {
                            Log.w(" Position is vertical", String.valueOf(axis));
                            battleship[i] = coordinates[0];
                            Log.w(" battleship[i]", String.valueOf(battleship[i]));
                            Log.w(" coordinates[0]", String.valueOf(coordinates[0]));
                            Log.w(" coordinates[1]", String.valueOf(coordinates[1]));
                            battleshipFinalCoordinates.add(i, coordinates[0] + " " + coordinates[1]);
                            boatsCoordinates[coordinates[0]][battleship[i]] = 1; // for the grid from @see FleetShip
                            coordinates[1]++;
                        }
                        // if axis of boat is horizontal
                        if(axis == 1) {
                            Log.w(" Position is horizontal", String.valueOf(axis));
                            battleship[i] = coordinates[1];
                            battleshipFinalCoordinates.add(i, coordinates[0] + " " + battleship[i]);
                            boatsCoordinates[coordinates[0]][battleship[i]] = 1;
                           // battleship[i]++;
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
        getboatsCoordinates();
        this.whereisBattleship = whereisBattleship;
    }
}
