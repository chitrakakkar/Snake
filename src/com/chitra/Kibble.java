package com.chitra;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.IOException;
/* In this game, Snakes eat Kibble. Feel free to rename to SnakeFood or Prize or Treats or Cake or whatever. */
public class Kibble extends JPanel
{
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	/** Identifies a random square to display a kibble
	 * Any square is ok, so long as it doesn't have any snake segments in it. 
	 * There is only one Kibble that knows where it is on the screen. When the snake eats the kibble, it doesn't disapear and
	 * get recreated, instead it moves, and then will be drawn in the new location. 
	 */

	private int kibbleX; //This is the square number (not pixel)
	private int kibbleY;  //This is the square number (not pixel)

	
	public Kibble(Snake s)
	{
		//Kibble needs to know where the snake is, so it does not create a kibble in the snake
		//Pick a random location for kibble, check if it is in the snake
		//If in snake, try again
		moveKibble(s);

	}
	// this basically generates(moves) the kibble
	protected void moveKibble(Snake s)
	{
		// generates the kibble randomly
		Random rng = new Random();
		boolean KibbleInMaze = true;
		boolean KibbleInSnake = true;
		boolean kibbleInSnakeANdMaze = true;
		//checks if
		while (kibbleInSnakeANdMaze == true)
		{
			//Generate random kibble location
			kibbleX = rng.nextInt(SnakeGame.xSquares);
			kibbleY = rng.nextInt(SnakeGame.ySquares);
			// check if kibble in snake segment?
			// yes-> loop executes till does not find a non-snake segment
			//else stays
			KibbleInSnake = s.isSnakeSegment(kibbleX, kibbleY); // checking if kibble in snake?
			KibbleInMaze = s.isMazeSegment(kibbleX,kibbleY);  // checking if kibble in maze?
			// if kibble is neither in maze/snake, donot have to search the square any more to draw..just draw
			if((KibbleInMaze == false)&&((KibbleInSnake==false)))
			{
				kibbleInSnakeANdMaze = false;
			}
		}
	}
	public int getKibbleX() {
		return kibbleX;
	}
	public int getKibbleY() {
		return kibbleY;
	}
}
