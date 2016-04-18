package com.chitra;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;


// This class designs the snake, its attributes and behavior//
// The attributes are: - Size,Head, Tail,segment to draw, filling the snake square with 1->2->3
// The behaviors -> movement,hitting the wall, eating tail, growing after eating tail
public class Snake
{
	final int DIRECTION_UP = 0;
	final int DIRECTION_DOWN = 1;
	final int DIRECTION_LEFT = 2;
	final int DIRECTION_RIGHT = 3;  //These are completely arbitrary numbers. 

	private boolean hitWall = false;
	private boolean ateTail = false;
	public boolean hitMaze = false;
	public HashMap<Integer,Integer> mazeCoordinates = new HashMap<>();



	public int snakeSquares[][];  //represents all of the squares on the screen
	//NOT pixels!
	//A 0 means there is no part of the snake in this square
	//A non-zero number means part of the snake is in the square
	//The head of the snake is 1, rest of segments are numbered in order

	private int currentHeading;  //Direction snake is going in, ot direction user is telling snake to go
	private int lastHeading;    //Last confirmed movement of snake. See moveSnake method
	
	private int snakeSize;   //size of snake - how many segments?

	private int growthIncrement = 1; //how many squares the snake grows after it eats a kibble

	private int justAteMustGrowThisMuch = 0;

	private int maxX, maxY, squareSize;  
	private int snakeHeadX, snakeHeadY; //store coordinates of head - first segment
	// constructor for snake -> takes
	public Snake(int maxX, int maxY, int squareSize)
	{
		this.maxX = maxX; // MaxX has value 10; got from SnakeGame class
		this.maxY = maxY; // Y also has 10 value from snakeGame class
		this.squareSize = squareSize;  // -> 50 -,snakeGame
		//Create and fill snakeSquares with 0s 
		snakeSquares = new int[maxX][maxY]; // creating squares on the frame and panel
		fillSnakeSquaresWithZeros();
		createStartSnake();
	}

	protected void createStartSnake()
	{
		//snake starts as 3 horizontal squares in the center of the screen, moving left
		// this case, the co-ordinates will be(5,5), since Xsqaures=10;Ysquares=10;
		int screenXCenter = (int) maxX/2;  //Cast just in case we have an odd number
		int screenYCenter = (int) maxY/2;  //Cast just in case we have an odd number
		//represents the snake body(Head-1, body-2,tail-3)
		snakeSquares[screenXCenter][screenYCenter] = 1;
		snakeSquares[screenXCenter+1][screenYCenter] = 2;
		snakeSquares[screenXCenter+2][screenYCenter] = 3;

		snakeHeadX = screenXCenter;
		snakeHeadY = screenYCenter;
		snakeSize = 3;
		//start direction of the snake
		currentHeading = DIRECTION_LEFT;// key stroke -> new direction for the snake
		lastHeading = DIRECTION_LEFT;// last known direction-> in progress
		justAteMustGrowThisMuch = 0;
	}
	// fills all the square drawn on the panel with zero
	//snake segment is non-zero
	public void fillSnakeSquaresWithZeros()
	{
		for (int x = 0; x < this.maxX; x++){
			for (int y = 0 ; y < this.maxY ; y++)
			{
				snakeSquares[x][y] = 0;

			}
		}
	}
	//this class returns a list of point to draw the snake.
	//squares containing 1->2->3
	public LinkedList<Point> segmentsToDraw()
	{
		//Return a list of the actual x and y coordinates of the top left of each snake segment
		//Useful for the Panel class to draw the snake
		LinkedList<Point> segmentCoordinates = new LinkedList<Point>();
		for (int segment = 1 ; segment <= snakeSize ; segment++ )
		{
			//search array for each segment number
			for (int x = 0 ; x < maxX ; x++)
			{
				for (int y = 0 ; y < maxY ; y++)
				{
					if (snakeSquares[x][y] == segment)
					{
						//make a Point for this segment's coordinates and add to list
						Point p = new Point(x * squareSize , y * squareSize);
						segmentCoordinates.add(p);
					}
				}
			}
		}
		return segmentCoordinates;
	}

	// kind of compels the snake to keep moving into the direction it was, when a key just opposite to it is pressed
	//eg: - if moving up and a down arrow is pressed, not to eat itself, compel it to keep moving up
	//applies to all these methods
	public void snakeUp()
	{
		if (currentHeading == DIRECTION_UP || currentHeading == DIRECTION_DOWN) { return; }
		currentHeading = DIRECTION_UP;
	}
	public void snakeDown()
	{
		if (currentHeading == DIRECTION_DOWN || currentHeading == DIRECTION_UP) { return; }
		currentHeading = DIRECTION_DOWN;
	}
	public void snakeLeft()
	{
		if (currentHeading == DIRECTION_LEFT || currentHeading == DIRECTION_RIGHT) { return; }
		currentHeading = DIRECTION_LEFT;
	}
	public void snakeRight()
	{
		if (currentHeading == DIRECTION_RIGHT || currentHeading == DIRECTION_LEFT) { return; }
		currentHeading = DIRECTION_RIGHT;
	}

//	public void	eatKibble(){
//		//record how much snake needs to grow after eating food
//		justAteMustGrowThisMuch += growthIncrement;
//	}
	// gives direction to the snake
	protected void moveSnake()
	{

		//Called every clock tick
		
		//Must check that the direction snake is being sent in is not contrary to current heading
		//So if current heading is down, and snake is being sent up, then should ignore.
		//Without this code, if the snake is heading up, and the user presses left then down quickly, the snake will back into itself.
		if (currentHeading == DIRECTION_DOWN && lastHeading == DIRECTION_UP) {
			currentHeading = DIRECTION_UP; //keep going the same way
		}
		if (currentHeading == DIRECTION_UP && lastHeading == DIRECTION_DOWN) {
			currentHeading = DIRECTION_DOWN; //keep going the same way
		}
		if (currentHeading == DIRECTION_LEFT && lastHeading == DIRECTION_RIGHT) {
			currentHeading = DIRECTION_RIGHT; //keep going the same way
		}
		if (currentHeading == DIRECTION_RIGHT && lastHeading == DIRECTION_LEFT) {
			currentHeading = DIRECTION_LEFT; //keep going the same way
		}
		
		//Did you hit the wall, snake? 
		//Or eat your tail? Don't move.
		if (isGameOver())
		{
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return;
		}

		if (wonGame())
		{
			SnakeGame.setGameStage(SnakeGame.GAME_WON);
			return;
		}

		//Use snakeSquares array, and current heading, to move snake

		//Put a 1 in new snake head square
		//increase all other snake segments by 1
		//set tail to 0 if snake did not just eat
		//Otherwise leave tail as is until snake has grown the correct amount
		//Find the head of the snake - snakeHeadX and snakeHeadY
		//Increase all snake segments by 1
		//All non-zero elements of array represent a snake segment

		for (int x = 0 ; x < maxX ; x++) // checks every x co-ordinate
        {
			for (int y = 0 ; y < maxY ; y++) // checks every y-co-ordinate
            {
                // if snake square is non-zero; basically 1or2or3
				if (snakeSquares[x][y] != 0)
                {
                    // add 1 to the segment to make it 2->3-> 4
                    // will replace 4 by 0 later;
					snakeSquares[x][y]++;
				}
			}
		}

		// subtracting 1 to make it 1->2->3 again from 2->3->
		//now identify where to add new snake head
		if (currentHeading == DIRECTION_UP) {		
			//Subtract 1 from Y coordinate so head is one square up
			snakeHeadY-- ;
		}
		if (currentHeading == DIRECTION_DOWN) {		
			//Add 1 to Y coordinate so head is 1 square down
			snakeHeadY++ ;
		}
		if (currentHeading == DIRECTION_LEFT) {		
			//Subtract 1 from X coordinate so head is 1 square to the left
			snakeHeadX -- ;
		}
		if (currentHeading == DIRECTION_RIGHT) {		
			//Add 1 to X coordinate so head is 1 square to the right
			snakeHeadX ++ ;
		}
		//Does this make snake hit the wall?
		//warp wall
        // if snake passes through 0 or 500 x co-ordinate(like ping pong)-> hits the wall
        // or passes through 0 or 500 y co-ordinate-> hits the wall

		// making the hitwall false makes the warp wall whenever hitting these co-ordinates
		//check if warp wall is true
		if(SnakeGame.UseWarpWall)
		{
			System.out.println("WarpWallActivated");
			if (snakeHeadX >= maxX || snakeHeadX < 0 || snakeHeadY >= maxY || snakeHeadY < 0)
			{
				int headXStorer = snakeHeadX;
				int headYStorer = snakeHeadY;
				hitWall = false;
				if (snakeHeadX < 0)
				{
					snakeHeadX = maxX; // gives the maxX co-ordinate to snake head to warp wall
					snakeSquares[headXStorer + snakeSize][headYStorer] = 0; // reset the value of tail to 0

				} else if (snakeHeadX >= maxX)
				{

					snakeHeadX = 0;
					snakeSquares[headXStorer - snakeSize][headYStorer] = 0;

				} else if (snakeHeadY < 0)
				{
					snakeHeadY = maxY;
					snakeSquares[headXStorer][headYStorer + snakeSize] = 0;
				} else if (snakeHeadY >= maxY)
				{
					snakeHeadY = 0;
					snakeSquares[headXStorer][headYStorer - snakeSize] = 0;
				}
				return;
			}
		}
			else
			{
				System.out.println("WarpWallNotActivated");
				if((snakeHeadX>=maxX || snakeHeadY<0)|| (snakeHeadX<0)||(snakeHeadY>maxY))
				{

					SnakeGame.setGameStage(SnakeGame.GAME_OVER);
					hitWall = true;
					return;
				}
			}
		//Does this make the snake eat its tail?
        // this tells that if snake enters the segment anything but 0
        // which is precisely its own body part
		if (snakeSquares[snakeHeadX][snakeHeadY] != 0)
		{
			ateTail = true;
			System.out.println("Ate tail"+"@ x = "+ snakeHeadX+" y="+snakeHeadY);
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);

			return;
		}


		//But if we get here, the snake has filled the screen. win!

		//Otherwise, game is still on. Add new head
        // 1 is the head part of the snake->; 2-> body->; 3-> tail
		snakeSquares[snakeHeadX][snakeHeadY] = 1;
		//If snake did not just eat, then remove tail segment
		//to keep snake the same length.
		//find highest number, which should now be the same as snakeSize+1, and set to 0
		if (justAteMustGrowThisMuch == 0)  // snake did not eat anything
        {
			for (int x = 0 ; x < maxX ; x++) // checks every square x-co ordinate
            {
				for (int y = 0 ; y < maxY ; y++) // checks every y-co-ordinate
                {
                    // if snake segment has 4 since added 1 to snake segment value
					if (snakeSquares[x][y] == snakeSize+1)
                    {
                        //make it 0 since it did not eat anything;just moving
						snakeSquares[x][y] = 0; //
					}
				}
			}
		}
        // justAteMustGrowThisMuch has value 1 here
		else
        {
			//Snake has just eaten. leave tail as is.  Decrease justAteMustGrowThisMuch variable by 1.
			justAteMustGrowThisMuch --; //Resetting the value back to 0;
			snakeSize ++;
		}
        // keeping track of direction change with respect to the current direction
		lastHeading = currentHeading; //Update last confirmed heading
	}
    // this method is for kibble to check if it is a snake segment
    // if a snake segment, a kibble would not be generated.
	public boolean isSnakeSegment(int kibbleX, int kibbleY)
    {
        // if kibble is drawn in 0 segment
		if (snakeSquares[kibbleX][kibbleY] == 0)
        {
			return false; // to break the loop in kibble
		}
		return true;
	}
    // check if snake eats the kibble
	public boolean didEatKibble(Kibble kibble)
    {
		//Is this kibble in the snake? It should be in the same square as the snake's head
		if (kibble.getKibbleX() == snakeHeadX && kibble.getKibbleY() == snakeHeadY){
			justAteMustGrowThisMuch += growthIncrement; // increases by 2;
			System.out.println("snake ate kibble");
			return true;
		}
		return false;
	}
	// checking the Maze Segment
	public boolean isMazeSegment(int kibbleX, int kibbleY)
	{
		for (Integer K:mazeCoordinates.keySet()
			 )
		{
			int MazeX = K;
			int MazeY = mazeCoordinates.get(K);
			if(((50*MazeX)==kibbleX ) && ((50 *MazeY)==kibbleY))
				return true;

		}
		return false;
	}
	// checking if snake has hit the maze
	public boolean didHitMaze( Mazes maze)
	{
			mazeCoordinates = maze.CreateMaze();
			for (Integer k : mazeCoordinates.keySet()
					)
			{
				if (((50 * k) == (snakeHeadX * squareSize)) && ((50 * mazeCoordinates.get(k) == (snakeHeadY * 50))))
				{
					hitMaze = true;
					return true;
				}
			}
		return false;
	}
    // not used in the program..
	public String toString()
    {
		String textsnake = "";
		//This looks the wrong way around. Actually need to do it this way or snake is drawn flipped 90 degrees.
		for (int y = 0 ; y < maxY ; y++) {
			for (int x = 0 ; x < maxX ; x++){
				textsnake = textsnake + snakeSquares[x][y];
			}
			textsnake += "\n";
		}
		return textsnake;
	}
    // this checks if the snake square still has some 0 left
    // if yes-> has not won the game yet
    // else, if only non-zero segment, win.
	public boolean wonGame()
	{

		//If all of the squares have snake segments in, the snake has eaten so much kibble 
		//that it has filled the screen. Win!
		for (int x = 0 ; x < maxX ; x++)
        {
			for (int y = 0 ; y < maxY ; y++)
            {
                // if all Snake squares on screen is still 0
				if (snakeSquares[x][y] == 0)
                {
					//there is still empty space on the screen, so haven't won
					return false;
				}
			}
		}
		//But if we get here, the snake has filled the screen. win!
		return true;
	}

	//starts the new game
	public void reset()
	{
		hitWall = false;
		ateTail = false;
		hitMaze = false;
		fillSnakeSquaresWithZeros();
		createStartSnake();

	}
	// returns true if hit wall or ate tail
	public boolean isGameOver()
	{
		if (hitWall || ateTail || hitMaze)
		{
			return true;
		}
		return false;
	}


}


