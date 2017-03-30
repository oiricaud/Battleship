package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Random;

// Controller

/**
 * @author Oscar Ivan Ricaud
 * @version 1.0
 *          Last update: 03/20/2017
 */
public class GameController extends AppCompatActivity implements Serializable {
    private GameModel computerPlayer = new GameModel();
    private GameModel humanPlayer = new GameModel();
    private MediaPlayer mp;

    /**
     * This is the main controller class and handles the creation of multiple views.
     *
     * @param savedInstanceState is the starting state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Call the corresponding controller
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
               // playMusic(this);
                // By default this is the first controller is called when the activity is created.
                launchHomeController();

                computerPlayer.setPlayerReady();
                computerPlayer.areYouPlaying(false);

                humanPlayer.setPlayerReady();
                humanPlayer.areYouPlaying(true);

            } else {
                String controller = extras.getString("controllerName");
                String player = extras.getString("player");

                // Gets called when the user begins the game
                assert controller != null;
                if (controller.equals("chooseLevelController")) {
                    // (view we launch, shouldWeStartGame?)
                    humanPlayer.updateModel(this, "chooseLevelView");
                }
                if (controller.equals("placeBoatsController")) {
                    computerPlayer.setDifficulty();
                    humanPlayer.updateModel(this, "placeBoatsView");
                }
                if (controller.equals("startGameView")) {
                    humanPlayer.updateModel(this, "startGameView");
                }
                assert player != null;
                if (controller.equals("updateBoat") && player.equals("human")) {
                    humanPlayer.setPlayerReady();
                    humanPlayer.updateModel(this, "startGameView");
                }
                if (controller.equals("updateBoat") && player.equals("computer")) {
                    computerPlayer.updateModel(this, "startGameView");
                }
                if (controller.equals("quitController")) {
                    finish();
                    launchHomeController();
                }
            }
        }
    }

    /**
     * The following controller is the beginning to this mobile application. It launches the home
     * screen and allows the user to begin a new game by first updating the @see GameModel to the
     * starting state, s0.
     */
    public void launchHomeController() {
        if (!humanPlayer.areYouPlaying() && !computerPlayer.areYouPlaying()) {
            humanPlayer.areYouPlaying(true);
            humanPlayer.updateModel(this, "launchHomeView");
        }
        //humanPlayer.updatePlayers(this, computerPlayer, humanPlayer);
        //   gameModel.updateView(this, "launchHomeView", "false"); // (view we launch, shouldWeStartGame?)
    }

    public void playMusic(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        // Play one of these random songs in the background.
        Random random = new Random();
        int obtainRandomNumber = random.nextInt(3 - 1 + 1) + 1;
        if (obtainRandomNumber == 1) {
            mp = MediaPlayer.create(context, R.raw.alterego);
        }
        if (obtainRandomNumber == 2) {
            mp = MediaPlayer.create(context, R.raw.oblivion);
        }
        if (obtainRandomNumber == 3) {
            mp = MediaPlayer.create(context, R.raw.yolo);
        }
        mp.start();
    }
}


