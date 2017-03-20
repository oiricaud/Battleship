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
    //private TextView counter;
    private Font eightBitFont = new Font("fonts/eightbit.TTF");

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


    /** This is the main controller class and handles the creation of multiple ships and board.
     //    *  @see Ship.java
     //   *  @see BoardView.java
     //   *  @see Board.java
     *  for more information.
     *
     *  At random, ships are placed either horizontally or vertically on a 10x10 board.
     *  The user is able to interact with this board and creates (x,y) coordinates.
     *  The user coordinates are compared to the coordinates from all boats that are randomly placed
     *  on the board. If the user hits a boat the method onDraw is invoked from the
     //   *  @see BoardView.java
     *  and colors a red circle the position of the boats, else colors a white circle indicating the
     *  user missed.
     *
     *
     * @param savedInstanceState is the starting state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                launchHomeController(); // Launch the first activity, the starting state, s0.
            } else{
                String controller = extras.getString("controllerName");
                if (controller.equals("computerPlaceBoatsController")) {
                    computerPlaceBoatsController(); // This means that the human has already placed the boats
                                            // and chosen a difficulty level
                }
                if (controller.equals("startGameController")) {
                    startGameController();
                }
                if (controller.equals("humanChooseLevelController")) {
                    humanChooseLevelController();
                }
            }
        }

    }
    /**
     * This is state 0, I will represent states such as s0, s1, s2, ... sn. Hopefully this makes it
     * easier to understand.
     *
     * The launch view is the beginning to this mobile application. It launches the home screen
     * and allows the user to begin a new game.
     *
     * s0 waits until the user clicks on the start button if the user clicks on the start
     * button the state diagram looks like the following: s0->s1, where s1 awaits for the user
     * input to choose a level of difficulty for the upcoming game.
     */
    private void launchHomeController(){
        setComputerReady(false);
        setHumanReady(false);
        Intent intent = new Intent(GameController.this, GameView.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("userType", "null");
        intent.putExtra("shouldWeStartGame", "false");
        intent.putExtra("viewWeWantToLaunch", "launchHomeView");
        GameController.this.startActivity(intent);
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    /** s1, Changes the current view where the user is able to choose from 3 buttons the following:
     *  easy, medium or hard, @see layout/activity_level for more details.
     */
    private void humanChooseLevelController() {
        setContentView(R.layout.activity_level);
        Button easy = (Button) findViewById(R.id.easy);
        Button medium = (Button) findViewById(R.id.medium);
        Button hard = (Button) findViewById(R.id.hard);
        TextView chooseLevel = (TextView) findViewById(R.id.chooseDifficulty);

        // Change font to a cooler 8-bit font.
        eightBitFont.changeFont(this, easy);
        eightBitFont.changeFont(this, medium);
        eightBitFont.changeFont(this, hard);
        eightBitFont.changeFont(this, chooseLevel);

        // Wait for the user input
        easy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("easy");
                callHumanPlaceBoatsView();  // Takes the user to @see GameView to place ships on the grid.
            }
        });
        medium.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("medium");
                callHumanPlaceBoatsView(); // Takes the user to @see GameView to place ships on the grid.
            }
        });
        hard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("hard");
                callHumanPlaceBoatsView(); // Takes the user to @see GameView to place ships on the grid.
            }
        });
    }
    /** Where the magic happens. Aka the function that allows the human to place boats on board view.
     *  s0->s1->s2
     *  @see GameView for more details.
     */
    private void callHumanPlaceBoatsView() {
        // The following is how you send data to other classes.
        setHumanReady(true);
        Intent intent = new Intent(GameController.this, GameView.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("viewWeWantToLaunch", "humanPlaceBoatsView");
        intent.putExtra("shouldWeStartGame", "false");
        GameController.this.startActivity(intent);
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    /** s0->s1->s2->s3
     *
     * Changes the content view to a more grey, unnerving background. Also changes the song to more
     * melancholy while attempting to petrify the user.
     *
     * Also it is the function where the computer places the boats on the grid at random.
     * Currently I would personally say that it is easy. Most of the times the boats will
     * be adjacent to each other.
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


