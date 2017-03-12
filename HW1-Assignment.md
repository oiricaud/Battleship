Homework 1 Assignment Requirements
======
### About the program
When you compile the program a cool background image should appear as well as a blue grid, with the title BattleShip, now in the background the boats are placed at random on the grid, horizontally or vertically. If the user misses, it makes a swoosh sound, else it makes a ka-boom sound. When the user taps on the board immediate feedback is given to the user, white circle = missed shot, red circle = non-missed shot.

Assignment #1
======

	CS 4330/5390: Mobile Application Development, Spring 2017
	HOMEWORK 1: Basic Android Programming and 2D Graphics
		 (File $Date: 2017/01/30 23:59:19 $)

Due: February 14, 2017

This homework may be done individually or in pair. Research has shown
that students learn better when they do homework assignments in pairs;
however, it does not necessarily mean a decrease in the time you will
spend on your assignments. 

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
   Android 2-D graphics. Conceptually, it's similar to Java 2-D
   graphics.
   
   The following Android framework classes and methods may be useful;
   read their API documents.

   android.app.DialogFragment or android.app.AlertDialog
   android.graphics.Canvas
   android.graphics.Color
   android.graphics.Paint
   android.view.View
   android.widget.Button
   android.widget.TextView
   android.widget.Toast
   
   void Canvas.drawCircle(float, float, float, Paint)
   void Canvas.drawLine(float, float, float, float, Paint)
   void Canvas.drawRect(float, float, float, float, Paint)
   void Paint.setColor(int)
   void View.onDraw(Canvas)
   void View.onTouch(MotionEvent) or
     void View.onTouchListener(View.OnTouchListener)

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
   use an AVD on your laptop. Prepare a 2 minutes demo of your app.

GRADING

   You will be graded on the quality of your design and how clear your
   code is. Excessively long code will be penalized: don't repeat code
   in multiple places. Your code should be well documented by using
   Javadoc comments and sensibly indented so it is easy to read.

   Be sure your name is in the comments in your source code.

REFERENCES 

   [1] Holger Gast, How to Use Objects, Addison-Wesley, 2016.
       Sections 9.1 and 9.2. Ebook available from UTEP library.

   [2] Martina Seidl, et al., UML@Classroom: An Introduction to
       Object-Oriented Modeling, Springer, 2015. Free ebook through
       UTEP library.


