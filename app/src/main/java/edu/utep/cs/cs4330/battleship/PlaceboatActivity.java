package edu.utep.cs.cs4330.battleship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_boat
 */

public class PlaceboatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_boat);
    }
}
