package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by oscarricaud on 2/10/17.
 */

public class AircraftcraftCarrier extends FleetShip {
        private int AircraftcraftCarrierSize = 5;
        private int numOfHits = 5;
        private boolean hasCoordinates = false;
        private int[] coordinates = new int[2];
        private boolean sunk;
        private int axis = 0; // 0 = horizontal, 1 = vertical
        private ArrayList<String> AircraftcraftCarrierFinalCoordinates = new ArrayList<String>();
        private ArrayList<String> whereisAircraftcraftCarrier;
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
                int coordinatesRange = (boardsize - AircraftcraftCarrierSize);

                // Set at random a position of the battleship which can be parallel to the x-axis or y-axis.
                axis = (int) (Math.random()*axisBound);
                coordinates[0] = (int) (Math.random() * coordinatesRange);
                coordinates[1] = (int) (Math.random() * coordinatesRange);

                // Debugging
                Log.w(" (x,y)", String.valueOf(coordinates[0]) + ", " + String.valueOf(coordinates[1]));

                for(int i = 0 ; i < AircraftcraftCarrierSize; i++){
                    // if axis of boat is vertical
                    if(axis == 0) {
                        //     Log.w(" Position is vertical", String.valueOf(axis));
                        AircraftcraftCarrierFinalCoordinates.add(i, coordinates[0] + " " + coordinates[1]);
                        coordinates[1]++;
                    }
                    // if axis of boat is horizontal
                    if(axis == 1) {
                        //      Log.w(" Position is horizontal", String.valueOf(axis));
                        AircraftcraftCarrierFinalCoordinates.add(i, coordinates[0] + " " + coordinates[1]);
                        coordinates[0]++;
                    }
                }
                Log.w("AircraftCarrier Pos:", String.valueOf(AircraftcraftCarrierFinalCoordinates));
                setAircraftcraftCarrier(AircraftcraftCarrierFinalCoordinates);
                hasCoordinates = true;
            }
            return coordinates;
        }

        public ArrayList<String> getAircraftcraftCarrierCoordinates() {
            return whereisAircraftcraftCarrier;
        }

        public void setAircraftcraftCarrier(ArrayList<String> whereisAircraftcraftCarrier) {
            // getboatsCoordinates();
            this.whereisAircraftcraftCarrier = whereisAircraftcraftCarrier;
        }

        public int getNumOfHits() {
            Log.w("Num of hits left", String.valueOf(numOfHits));
            return numOfHits;
        }

        public void setNumOfHits(String coordinates) {
            if(!placesthathavebeenhit.contains(coordinates)){
                placesthathavebeenhit.add(coordinates);
                numOfHits = numOfHits - 1;
                if(numOfHits == 0){
                    setSunk();
                }
            }
            else{
                Log.w("Already hit that place", "");
            }
        }

        public boolean isSunk() {
            return false;
        }

        public void setSunk() {
            this.sunk = true;
        }
}

