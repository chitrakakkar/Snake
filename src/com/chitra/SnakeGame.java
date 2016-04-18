package com.chitra;

import java.awt.*;
import java.util.Timer;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import javax.swing.*;
// This class basically has the main method which creates the GUI and initializes the game
//To initialize the game, it creates a component manager which handles snake, kibble, scores.
// also, creates a snake, kibble which adds it to the panel.
public class SnakeGame
{
	//Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public final static int xPixelMaxDimension = 501;  //can change it
	public final static int yPixelMaxDimension = 501;

	public static int xSquares ;
	public static int ySquares ;

	public final static int squareSize = 50;//can change it

	protected static Snake snake ;
	protected static Mazes mazes;

	private static GameComponentManager componentManager;

	protected static Score score;

	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;   //The numerical values of these variables are not important. The important thing is to use the constants
	//instead of the values so you are clear what you are setting. Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables 
	//using statements such as SnakeGame.GAME_OVER 

	private static int gameStage = BEFORE_GAME;  //use this to figure out what should be happening.

	//Other classes like Snake and DrawSnakeGamePanel will query this, and change its value

	// three speed options to give user the choice of changing the speed of the game
	static final long Slower_Option_for_SpeedChange = 1000;
	static final long Faster_Option_for_SpeedChange = 300;
	static final long Default_Speed = 0;

	protected static long clockInterval = Default_Speed ; //controls game speed
	//  Bug-1-> if you increase the time interval, it does not move immediately
	// when

	public static boolean UseWarpWall =false;
	public static boolean USeMazeWall = false;

	public static void setUSeMazeWall(boolean USeMazeWall)
	{
		SnakeGame.USeMazeWall = USeMazeWall;
	}

	public static void setUseWarpWall(boolean useWarpWall)
	{
		UseWarpWall = useWarpWall;
	}
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1 second.

	static JFrame snakeFrame;
	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html

	public static void main(String[] args)
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		//Swing event handling runs on a special thread known as EDT.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				initializeGame();
				createAndShowGUI();
			}
		});
	}

	public static DrawSnakeGamePanel getSnakePanel()
	{
		return snakePanel;
	}

	private static void createAndShowGUI()
	{
		//Create and set up the window.
		// a jframe object-> extends frame class which extends windows
		// and implements MenuContainer
		snakeFrame = new JFrame();
		// // Using WindowConstant interface-Constants used to control the window-closing operation
		snakeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
		snakeFrame.setUndecorated(true); //hide title bar
		snakeFrame.setVisible(true);
		snakeFrame.setResizable(false);
		snakePanel = new DrawSnakeGamePanel(componentManager);
		snakePanel.setFocusable(true);
		snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents
		snakeFrame.add(snakePanel);
		//Add listeners to listen for key presses
		snakePanel.addKeyListener(new GameControls());
		snakePanel.addKeyListener(new SnakeControls(snake));
		setGameStage(BEFORE_GAME);
		snakeFrame.setVisible(true);
	}
	// a method to get all the componets like snake, kibble, maze, scores which make
	private static void initializeGame()
	{

		//set up score, snake and first kibble
		xSquares = xPixelMaxDimension / squareSize; // 10 in this case(501/50)
		ySquares = yPixelMaxDimension / squareSize; // 10
		// creating new component manager object,snake object,kibble and maze object
		componentManager = new GameComponentManager();
		snake = new Snake(xSquares, ySquares, squareSize);
		Kibble kibble = new Kibble(snake);
		//Mazes maze = new Mazes(snake);
		Mazes maze = new Mazes();
		componentManager.addSnake(snake);
		componentManager.addKibble(kibble);
		componentManager.addMazes(maze);
		score = new Score();
		componentManager.addScore(score);
		gameStage = BEFORE_GAME;
	}
	// new game method
	public static void newGame()
	{
		// new timer object
		Timer timer = new Timer();
		// every clock tick ->takes care of the game display and updates the game;
		GameClock clockTick = new GameClock(componentManager, snakePanel);
		// // resets snake where hit wall=0;hit tail=false;fill snake with zero ;create start snake
		componentManager.newGame();

		//Schedules the specified task for repeated fixed-rate execution, beginning at the specified time
		timer.scheduleAtFixedRate(clockTick, 0, clockInterval);
		// clock tick is the specified task,0=delay,clock interval = period.
	}
	public static int getGameStage() {
		return gameStage;
	}
	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}
	public static void setGameSpeed(long speedChosen)
	{
		clockInterval = speedChosen;
	}
	public static long getGameSpeed()
	{
		return clockInterval;
	}

	// a method to activate Warp wall on/off based on key pressed
	public static void WarpWallOnOrOff(String wPressed)
	{
		if(wPressed.equals("w")|| wPressed.equals("W"))
		{
			setUseWarpWall(true);

		}
		else
		{
			setUseWarpWall(false);
		}
	}
	// this method activates Maze on/off based on key pressed
	public static void MazeWallOffOROn(String mPressed)
	{

		if(mPressed.equals("m")|| mPressed.equals("M"))
		{
			setUSeMazeWall(true);
		}
		else
		{
			setUSeMazeWall(false);
		}
	}
}
