package edu.utep.cs.cs4330.battleship;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by oscarricaud on 3/31/17.
 */
public class ChoiceToucheListener implements View.OnTouchListener {
    private int _xDelta;
    private int _yDelta;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // TODO Auto-generated method stub
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = 500;
                layoutParams.topMargin = 50;
                view.setLayoutParams(layoutParams);
                break;
        }
        view.invalidate();
        return true;
    }
}
