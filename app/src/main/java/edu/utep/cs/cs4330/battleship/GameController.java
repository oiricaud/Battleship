package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Controller
/**
 * @author Oscar Ivan Ricaud
 * @version 1.0
 * Last update: 02/23/2017
 */
public class GameController extends AppCompatActivity {
    private String difficulty;
    private boolean isComputerReady;
    private boolean isHumanReady;

    /* Begin Setters and Getters */
        /**
         * @return this returns the difficulty the user chose, which is later retrieved to be displayed
         * on the android phone.
         */
        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public boolean isComputerReady() {
            return isComputerReady;
        }

        public void setComputerReady(boolean computerReady) {
            isComputerReady = computerReady;
        }

        public boolean isHumanReady() {
            return isHumanReady;
        }

        public void setHumanReady(boolean humanReady) {
            isHumanReady = humanReady;
        }
    /* End Setters and Getters */

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
            }
        }
    }
    /**
     * The following controller is the beginning to this mobile application. It launches the home screen
     * and allows the user to begin a new game.
     *
     * It does not call the next controller until the user clicks on the start button.
     */
    private void launchHomeController(){
        setComputerReady(false);
        setHumanReady(false);
        Intent intent = new Intent(GameController.this, GameView.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("shouldWeStartGame", "false");
        intent.putExtra("viewWeWantToLaunch", "launchHomeView");
        GameController.this.startActivity(intent);
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    /** This controller calls the view, where the user is able to choose the level of difficulty
     *  easy, medium or hard, @see layout/activity_level for more details.
     */
    private void humanChooseLevelController() {
        // The following is how you send data to other classes.
        Intent intent = new Intent(GameController.this, GameView.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("shouldWeStartGame", "false");
        intent.putExtra("viewWeWantToLaunch", "humanChooseLevelView");
        GameController.this.startActivity(intent);
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setHumanReady(true);
    }
    /**
     * This controller handles the coordinates of the boats at random
     * @see GameView for more details.
     */
    private void computerPlaceBoatsController() {
        // The following is how you send data to other classes.
        setComputerReady(true);
        Intent intent = new Intent(GameController.this, edu.utep.cs.cs4330.battleship.GameController.class);
        intent.putExtra("controllerName", "startGameView");
        GameController.this.startActivity(intent);
        /** Fading Transition Effect */
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * This controller gets called once the user & AI has placed all boats on the grid.
     */
    private void startGameController() {
        // The following is how you send data to other classes.
        Intent intent = new Intent(GameController.this, GameView.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("viewWeWantToLaunch", "startGameView");
        intent.putExtra("shouldWeStartGame", "true");
        GameController.this.startActivity(intent);
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}


