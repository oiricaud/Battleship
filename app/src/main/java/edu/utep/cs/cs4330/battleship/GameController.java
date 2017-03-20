package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

// Controller
/**
 * @author Oscar Ivan Ricaud
 * @version 1.0
 * Last update: 02/23/2017
 */
public class GameController extends AppCompatActivity  {
    private GameModel gameModel = new GameModel();

    /** This is the main controller class and handles the creation of multiple views.
     * @param savedInstanceState is the starting state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call the corresponding controller methods
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                // By default this is the first controller is called when the activity is created.
                launchHomeController();
            } else{
                String controller = extras.getString("controllerName");
                // Gets called when the user begins the game
                if (controller.equals("humanChooseLevelController")) {
                    humanChooseLevelController();
                }
                // This controller handles the placement of random boats for the computer
                if (controller.equals("computerPlaceBoatsController")) {
                    computerPlaceBoatsController();
                }
                // This controller handles the creation of the Battleship Game view.
                if (controller.equals("startGameController")) {
                    startGameController();
                }
                if(controller.equals("humansTurnController")){
                    humansTurnController();
                }
                if(controller.equals("computersTurnController")){
                    computersTurnController();
                }
            }
        }
    }
    /**
     * The following controller is the beginning to this mobile application. It launches the home screen
     * and allows the user to begin a new game.
     *
     * It does not call the next controller until the user clicks on the start button.
     */
    public void launchHomeController(){
        gameModel.setComputerStatus(false);
        gameModel.setUserStatus(false);
        gameModel.updateView(this, "launchHomeView", "false"); // (view we launch, shouldWeStartGame?)
        fadingTransition();
    }
    /** This controller calls the view, where the user is able to choose the level of difficulty
     *  easy, medium or hard, @see layout/activity_level for more details.
     */
    public void humanChooseLevelController() {
        gameModel.setUserStatus(true);
        gameModel.updateView( this, "humanChooseLevelView", "false"); // (view we launch, shouldWeStartGame?)
        fadingTransition();
    }
    /**
     * This controller handles the coordinates of the boats at random
     * @see GameView for more details.
     */
    public void computerPlaceBoatsController() {
        // The following is how you send data to other classes.
        gameModel.setComputerStatus(true);
        gameModel.updateView(this, "startGameView", "true");
        fadingTransition();
    }
    /**
     * This controller gets called once the user & AI has placed all boats on the grid.
     */
    public void startGameController() {
        // The following is how you send data to other classes
        gameModel.updateView( this, "startGameView", "true"); // (view we launch, shouldWeStartGame?)
        fadingTransition();
    }
    public void humansTurnController() {
    }

    public void computersTurnController() {
    }

    private void fadingTransition() {
        /** Fading Transition Effect */
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}


