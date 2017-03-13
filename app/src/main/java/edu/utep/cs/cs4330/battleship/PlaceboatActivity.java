package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by oscarricaud on 3/12/17.
 * This activity will allow the user to place boats. @see activity_place_boat
 */

public class PlaceboatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_boat);

        TextView title = (TextView) findViewById(R.id.placeboats);
        Button next = (Button) findViewById(R.id.next);

        // Change font
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/eightbit.TTF");
        title.setTypeface(typeface);
        next.setTypeface(typeface);

        // 
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceboatActivity.this, GameActivity.class);
                PlaceboatActivity.this.startActivity(intent);
                /** Fading Transition Effect */
                PlaceboatActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
