package edu.utep.cs.cs4330.battleship;

import android.app.Fragment;
import android.os.Bundle;

/**
 *
 * Created by oscarricaud on 4/8/17.
 */
public class RetainedFragment extends Fragment {

    // data object we want to retain
    private GameModel gameState = new GameModel();
    private String currentView;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    GameModel getCurrentGameState() {
        return gameState;
    }

    public void setData(GameModel data) {
        this.gameState = data;
    }

    String getCurrentView() {
        return currentView;
    }

    void setCurrentView(String currentView) {
        this.currentView = currentView;
    }
}