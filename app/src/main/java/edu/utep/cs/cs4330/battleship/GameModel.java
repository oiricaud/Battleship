package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.content.Intent;

/**
 *
 * Created by oscarricaud on 3/19/17.
 */

class GameModel {

    GameModel() {
    }

    /**
     * @return this returns the difficulty the user chose, which is later retrieved to be displayed
     * on the android phone.
     */

    void updateModel(Context context, String viewWeWantToLaunch) {
        Intent intent = new Intent(context, GameView.class);
        intent.putExtra("viewWeWantToLaunch", viewWeWantToLaunch);
        context.startActivity(intent);
    }
}
