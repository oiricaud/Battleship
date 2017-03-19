package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private BoardView boardView;

    private MediaPlayer mp;
    //private TextView counter;
    private Font eightBitFont = new Font("fonts/eightbit.TTF");
    private Music shipSound = new Music();
    private Music gamePlayMusic = new Music();
    private String difficulty;

    /** Setters and Getters
     * @return this returns the difficulty the user chose, which is later retrieved to be displayed
     * on the android phone.
     */
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

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

        if(savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
            {
                launchView(); // Launch the first activity, the starting state, s0.
            }else{
                String method = extras.getString("methodName");

                if (method.equals("computerPlaceBoatsView"))
                {
                    computerPlaceBoatsView(); // This means that the human has already placed the boats
                                            // and chosen a difficulty level
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
    private void launchView(){
        setContentView(R.layout.home);
        gamePlayMusic.playMusic(this);
        TextView battleshipLabel = (TextView) findViewById(R.id.BattleShip); // Change font
        eightBitFont.changeFont(this, battleshipLabel);

        // Begin to the next activity, placing boats on the map
        Button startButton = (Button) findViewById(R.id.start);
        eightBitFont.changeFont(this, startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLevelView(); // Ask the user what Level of difficulty do they want to play on.
            }
        });
    }
    /** s1, Changes the current view where the user is able to choose from 3 buttons the following:
     *  easy, medium or hard, @see layout/activity_level for more details.
     */
    private void chooseLevelView() {
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
                humanPlaceBoatsView();  // Takes the user to @see PlaceShips to place ships on the grid.
            }
        });
        medium.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("medium");
                humanPlaceBoatsView(); // Takes the user to @see PlaceShips to place ships on the grid.
            }
        });
        hard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDifficulty("hard");
                humanPlaceBoatsView(); // Takes the user to @see PlaceShips to place ships on the grid.
            }
        });
    }
    /** s0->s1->s2->s3
     *
     * Changes the content view to a more grey, unnerving background. Also changes the song to more
     * melancholy while attempting to petrify the user.
     *
     * Also it is the function where the computer places the boats on the grid at random.
     * Currently I would personally say that it is easy. Most of the times the boats will
     * be adjacent to each other.
     */
    private void computerPlaceBoatsView() {
        // The following is how you send data to other classes.
        Intent intent = new Intent(GameController.this, PlaceShips.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("userType", "computer");
        GameController.this.startActivity(intent);
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    /** Where the magic happens. Aka the function that allows the human to place boats on board view.
     *  s0->s1->s2
     *  @see PlaceShips for more details.
     */
    private void humanPlaceBoatsView() {
        // The following is how you send data to other classes.
        Intent intent = new Intent(GameController.this, PlaceShips.class);
        String level_of_difficulty = String.valueOf(getDifficulty());
        intent.putExtra("level_of_difficulty", level_of_difficulty); // YOUR key, variable you are passing
        intent.putExtra("userType", "human");
        GameController.this.startActivity(intent);
        GameController.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}


