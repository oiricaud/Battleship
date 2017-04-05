package edu.utep.cs.cs4330.battleship;

import android.content.ClipData;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

/*
 * Created by oscarricaud on 3/31/17.
 */
public class MyTouchListener implements View.OnTouchListener {
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            } else {
                //noinspection deprecation
                view.startDrag(data, shadowBuilder, view, 0);
            }
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
