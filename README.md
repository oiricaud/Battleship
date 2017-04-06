Battleship
======
### About the program
Update 1.1 
 *This version allows the user to place boats on the grid while playing a game with the computer.*

To view the Java classes for this project [Click Here](https://github.com/oiricaud/Battleship/tree/master/app/src/main/java/edu/utep/cs/cs4330/battleship).

Visual representations of Class Diagrams [Click Here](https://github.com/oiricaud/Battleship/blob/master/Class-Diagram.png).

To view Assignment #2  [Click Here](https://github.com/oiricaud/Battleship/blob/master/HW2-Assignment.md).

To view the requirements for Assignment #1  [Click Here](https://github.com/oiricaud/Battleship/blob/master/HW1-Assignment.md).


Assignment #3 
======

	 CS 4330/5390: Mobile Application Development, Spring 2017
		HOMEWORK 2: UI, Activities and Intents
		 (File $Date: 2017/02/21 02:52:45 $)

Due: March 20, 2017

This homework may be done individually or in pair. 

In this homework you will extend your HW1 code to allow a user to play
the Battleship game against the computer. For this, you will extend
the UI to deploy the user's fleet of ships and play against the
computer. You will also design a shooting strategy for the computer to
decide its shots. The most common playing rule is for players to
alternate firing at each other. We will change the rule to let players
keep their turns as long as they hit ships. Your app shall meet the
following functional and non-functional requirements.


You will be developing an Android application that allows one to play
Battleship game. Battleship is a guessing game for two players
[Wikipedia]. The game is played on grids, usually 10x10, of
squares. Each player has a fleet of ships, and each ship occupies a
number of consecutive squares on the grid, arranged either
horizontally or vertically. Once the ships are secretly positioned on
the grids of the players, the game proceeds in a series of rounds. In
each round, each player takes a turn to make a shot to a square in the
opponent's grid. A shot is either a "hit" on a ship or a "miss". When
all of the squares of a ship have been hit, the ship sinks. If all of
a player's ships have been sunk, the game is over and the opponent
wins.

In this homework, you are to write the first version of the Battleship
app that lets a user to hit and sink ships secretly placed on a board.
You may use the base code available from the course website. Your app
should meet the following requirements.

R1. It shall present a 2-D representation of a single board where
    ships are placed secretly. The fleet of ships shall be placed
    randomly one the board. Use the BoardView class included in the
    base code to display a board using 2-D graphics. Refer to Section
    5.3 (Drawing and Canvas class) of the textbook for Android 2-D
    graphics.
   
R2. The fleet of ships to be placed on the board shall consist of the
    following five ships.

    Aircraft carrier (of size 5)
    Battleship (of size 4)
    Frigate (of size 3)
    Submarine (of size 3)
    Minesweeper (of size 2)

R3. It shall show the progress of a game by displaying the current
    state of the board---e.g., places hit and ships hit/sunk---along
    with other information such as the number of shots.
   
R4. It shall allow a user to play a new game. Use the "New" button
    provided by the base code.

R5. You should use the Model-View-Control (MVC) metaphor [1], and your
    model classes should be completely separated from the view and
    control classes. There should be no dependency from model classes
    to view/control classes. To make your design extensible for future
    improvements such as strategy and network games.

R6. Use the following configuration for your project.

    Application name: Battleship
    Company domain: cs4330.cs.utep.edu
    Min SDK version: API 14 - Android 4.0 (IceCreamSandwich)
    Main activity: MainActivity

    Name your distribution apk file as battleship.apk (see the
    submission instruction below).

1. (15 points) Design your app and express your design in a UML class
   diagram [2]. Your class diagram should include both model classes
   and UI classes (see R5 above) by using a layered architecture,
   where the business logic layer is completely separated from the
   presentation layer.

   - Your class diagram should show the main components (classes) 
     of your app, their roles and their relationships. 
   - Your model (business logic) classes should be cleanly separated 
     from the view/control (UI) classes with no dependency.
   - For each class in your diagram, define key (public) operations
     to show its roles or responsibilities.
   - For each association including aggregate and composite, include
     at least a label, multiplicities and directions.
   - For each class appearing in your class diagram, provide a brief 
     description.

2. (85 points) Code your design. Your code should conform to your
   design.

3. (5 bonus points) Provide a custom launch icon for your app. Use
   online tools such as Android Asset Studio to create custom icons
   (https://romannurik.github.io/AndroidAssetStudio/)

4. (5 bonus points) Improve the implementation of R4---starting a new
   game). If a game is already in progress, it should use an alert
   dialog to get a confirmation from the user (use DialogFragment or
   AlertDialog).

5. (10 bonus points) Improve user experience of your app by adding
   some sound effect, e.g., playing sounds when a place is hit, a
   ship is sunk, and the game is over (read Section 5.7 Audio).

TESTING

   Your code should compile using Android 6.0 or later version and run
   correctly on Android 4.0 (API Level 14) and later.

HINTS

   Download the base code from the course website and import it to
   Android Studio; use File > New > Import Project... 

   Refer to Section 5.3 (Drawing and Canvas class) of the textbook for
R1-R6 of HW1 requirements (see HW1 handout)

R1. It shall provide a good shooting strategy for the computer to pick
    the next place to shoot. When the computer hits a ship, it shall
    sink the ship by shooting the adjacent places of the hit. Create
    your own algorithm or use a popular one like the "hunt and target"
    strategy [1]. Consider using the Strategy design pattern [2] to
    provide more than one strategy (see Problem 5 below).

R2. It shall provide a UI to deploy the user's fleet of ships. The UI
    shall allow the user to specify the places and the direction of
    each ship (see R2 of HW1 for the fleet of ships).

R3. It shall show the progress of a game by displaying the current
    state of the game, including the two boards, the user's ships, the
    turn, and the game outcome (see R1 and R3 of HW1). As in HW1, the
    boards shall be displayed using 2D graphics.

R4. It shall consist of at least two activities, one for deploying the
    user's ships (R2) and the other for making shots (R3).

R5. It shall support screen orientation changes for the second
    activity of R4 above. It shall store and restore the game state
    when the screen orientation changes. You will need to define two
    different layouts: portrait and landscape.

R6. Use the following configuration for your project.

    Application name: Battleship
    Company domain: cs4330.cs.utep.edu
    Min SDK version: API 14 - Android 4.0 (IceCreamSandwich)
    Main activity: MainActivity

    Name your signed distribution apk file as battleship.apk (see the
    submission instruction below).

1. (15 points) Design your app and express your design in a UML class
   diagram [3]. Your class diagram should include both model classes
   and UI classes (see HW1 R5) by using a layered architecture, where
   the business logic layer is completely separated from the
   presentation layer.

   - Your class diagram should show the main components (classes) 
     of your app, their roles and their relationships. 
   - Your model (business logic) classes should be cleanly separated 
     from the view/control (UI) classes with no dependency.
   - For each class in your diagram, define key (public) operations
     to show its roles or responsibilities.
   - For each association including aggregate and composite, include
     at least a label, multiplicities and directions.
   - For each class appearing in your class diagram, provide a brief 
     description.

2. (85 points) Code your design. Your code should conform to your
   design.

3. (5 bonus points) Provide an action icon to enable/disable the sound
   effect (see HW1 Problem 5).
   
4. (10 bonus points) Provide an animated transition when navigating
   between activities (read Sections 3.6, 3.7 and 4.6).

5. (10 bonus points) Improve the implementation of R1 by supporting
   more than one shooting strategy and providing a menu to select one
   (or game level).

HINTS
   
   Reuse your HW1 design and code as much as possible. Organize your
   classes in a class hierarchy. You will have several classes with
   similar structures and behaviors, e.g., different board views.

TESTING

   Your code should compile using SDK version 25 and run correctly on
   Android 4.0 (API Level 14) and later.

WHAT AND HOW TO TURN IN

   You should submit your program through the Assignment Submission
   page found in the Homework page of the course website. You should
   submit a single zip file that contains:

   - A UML class diagram and associated documents (if any)
   - An Android package (APK) file. Read Section 1.9 (Sharing Your 
     Android Applications) for creating an Android distribution file.
     On Android Studio, use Build > Generate Singed APK ... menu.
   - Source code---the Java src directory in your project folder 
     (app/src/main/java). Include only Java source code files; do 
     not include other files like resource/build files.

   Your zip file should contain a single root directory named
   YourFirstNameLastName in which all the above-mentioned files and
   directories reside, e.g.,

   - readme.txt (if you have anything to say to TA)
   - design.doc (UML class diagram along with descriptions)
   - battleship.apk (signed APK file)
   - src/... (Java source code files and directories)

   If you work in pair, make only one submission by mentioning both
   names. You should turn in your program by midnight on the due date.

DEMO

   You will do an demo of your app, either in-class or to TA. Bring a
   WiFi-capable Android device with your app installed; you may also
   use an AVD on your laptop. Prepare a 2~3 minutes demo of your app.

GRADING

   You will be graded on the quality of your design and how clear your
   code is. Excessively long code will be penalized: don't repeat code
   in multiple places. Your code should be well documented by using
   Javadoc comments and sensibly indented so it is easy to read.

   Be sure your name is in the comments in your source code.

REFERENCES 

   [1] Aisha Harris, How to Win at Battleship. 
       http://www.slate.com/blogs/browbeat/2012/05/16/
       _battleship_how_to_win_the_classic_board_game_every_time.html
       (Retrieved: February 19, 2017)

   [2] Holger Gast, How to Use Objects, Addison-Wesley, 2016.
       Sections 9.1 and 9.2. Ebook available from UTEP library.

   [3] Martina Seidl, et al., UML@Classroom: An Introduction to
       Object-Oriented Modeling, Springer, 2015. Free ebook through
       UTEP library.
