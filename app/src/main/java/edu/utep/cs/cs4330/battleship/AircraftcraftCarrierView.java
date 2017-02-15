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
import java.util.List;
import java.util.Random;

/**
 * Created by oscarricaud on 2/10/17.
 */

public class AircraftcraftCarrierView extends View {

        private final int hitColor = Color.rgb(220,20,60);
        private final Paint hitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        {
            hitPaint.setColor(hitColor);
        }
        public AircraftcraftCarrierView(Context context)
        {
            super(context);
            init(context);
        }

        public AircraftcraftCarrierView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            init(context);
        }

        public AircraftcraftCarrierView(Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
            init(context);
        }

        private void init(Context context)
        {
            Log.w("Inside the ac", "init");
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            Log.w("onDraw", "onDraw");
            // check the state of each place of the board and draw it.
            // Log.w("FleetShip Coordinates:", String.valueOf(getboatsCoordinates));
            final float maxCoord = 10;
            final float placeSize = 10;
            canvas.drawRect(0, 0, maxCoord, maxCoord, hitPaint);
        }
    }