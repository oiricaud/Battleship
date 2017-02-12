package edu.utep.cs.cs4330.battleship;

import android.util.Log;

import java.util.ArrayList;

public class Battleship extends FleetShip {
    private int battleshipSize = 4;
    private boolean hasCoordinates = false;
    private int[] coordinates = new int[2];
    private int position = 0; // Default position is set to horizontal, 1 = Vertical
    private int [] battleship = new int [battleshipSize];
    private ArrayList<String> battleshipFinalCoordinates = new ArrayList<String>();
    private ArrayList<String> whereisBattleship;

    public void placeShip(int boardsize) {
        int[] coorRandom = getRandomCoordinates(boardsize);
        coordinates[0] = coorRandom[0];
        coordinates[1] = coorRandom[1];
    }

    private int[] getRandomCoordinates(int boardsize) {
        int [] coordinates = new int[2];
                if(!hasCoordinates) {
                    int randPosition = 2;
                    int range = (boardsize - battleshipSize);
                    position = (int) (Math.random()*randPosition);
                    coordinates[0] = (int) (Math.random() * range);
                    coordinates[1] = (int) (Math.random() * range);
                    Log.w("Battleship range", String.valueOf(range));
                    Log.w(" Has coordinates", String.valueOf(hasCoordinates));
                    Log.w(" x", String.valueOf(coordinates[0]));
                    Log.w(" y", String.valueOf(coordinates[1]));
                    Log.w(" (x,y)", String.valueOf(coordinates[0]) + ", " + String.valueOf(coordinates[1]));
                    for(int i = 0 ; i < battleshipSize; i++){
                        if(position == 0) { // Position of boat is horizontal
                            Log.w(" Position is horizontal", String.valueOf(position));
                            battleship[i] = coordinates[0];
                            battleshipFinalCoordinates.add(i, coordinates[0]+ " "+ coordinates[1]);
                            boatsCoordinates[coordinates[0]][battleship[i]] = 1;
                            coordinates[1]++;
                        }
                        if(position == 1) { // Position of boat is vertical
                            Log.w(" Position is vertical", String.valueOf(position));
                            battleship[i] = coordinates[1];
                            battleshipFinalCoordinates.add(i, coordinates[0]+ " "+ battleship[i]);
                            boatsCoordinates[coordinates[0]][battleship[i]] = 1;
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
