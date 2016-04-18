package com.chitra;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;

/** This class responsible for displaying the graphics, so the snake, grid, kibble, instruction text and high score
 * 
 * @author Clara
 *
 */
public class DrawSnakeGamePanel extends JPanel
{
	public static Color green = new Color(	14350246);
	public static Color snakeHeadColor = new Color(3348795);
	private static int gameStage = SnakeGame.BEFORE_GAME;  //use this to figure out what to paint
	private BufferedImage image;
	
	private Snake snake;
	private Kibble kibble;
	private Score score;
	private Mazes mazes;

	DrawSnakeGamePanel(GameComponentManager components)
	{
		this.snake = components.getSnake();
		this.kibble = components.getKibble();
		this.score = components.getScore();
		this.mazes = components.getMazes();
		;
	}
	public Dimension getPreferredSize()
	{
        return new Dimension(SnakeGame.xPixelMaxDimension, SnakeGame.yPixelMaxDimension);
    }
	@Override
    public void paintComponent(Graphics g)
	{
		super.paintComponent(g);


          // extending from Jframe
		// ->Calls the UI delegate's paint method


        /* Where are we at in the game? 4 phases.. 
         * 1. Before game starts
         * 2. During game
         * 3. Game lost aka game over
         * 4. or, game won
         */

        gameStage = SnakeGame.getGameStage();

        
        switch (gameStage) {
			case SnakeGame.BEFORE_GAME: {
				displayInstructionsAndOption(g);
				break;
			}
			case SnakeGame.DURING_GAME:
			{

				displayGame(g);
				break;
			}
			case SnakeGame.GAME_OVER:
			{
				displayGameOver(g);
				break;
			}
			case SnakeGame.GAME_WON:
			{
				displayGameWon(g);
				break;
        	}
        }
    }

	private void displayGameWon(Graphics g)
	{
		// TODO Replace this with something really special!
		g.clearRect(100,100,350,350);
		g.drawString("YOU WON SNAKE!!!", 150, 150);
		
	}
	private void displayGameOver(Graphics g)
	{


		//g.clearRect(100,100,350,350);
		g.fillRect(100,100,350,350);
		g.setColor(Color.PINK);
		g.drawString("GAME OVER", 150, 150);
		
		String textScore = score.getStringScore();
		String textHighScore = score.getStringHighScore();
		String newHighScore = score.newHighScore();
		
		g.drawString("SCORE = " + textScore, 150, 250);
		
		g.drawString("HIGH SCORE = " + textHighScore, 150, 300);
		g.drawString(newHighScore, 150, 400);
		
		g.drawString("press a key to play again", 150, 350);
		g.drawString("Press q to quit the game",150,400);
		//g.drawString("Press q to quit the game",150,450);
    			
	}

	private void displayGame(Graphics g)
	{
		displayGameGrid(g);
		displaySnake(g);
		displayKibble(g);
		displayMazes(g);
	}
	private void displayGameGrid(Graphics g)
	{
		int maxX = SnakeGame.xPixelMaxDimension;
		int maxY= SnakeGame.yPixelMaxDimension;
		int squareSize = SnakeGame.squareSize;
		
		//g.clearRect(0, 0, maxX, maxY);
		//filled the rectangles with green color
		g.setColor(green);
		g.fillRect(0,0,maxX,maxY);
		//g.fill3DRect(0,0,maxX,maxX,true);

		g.setColor(green);
		//Draw grid - horizontal lines
		for (int y=0; y <= maxY ; y+= squareSize)
		{
			g.drawLine(0, y, maxX, y);
		}
		//Draw grid - vertical lines
		for (int x=0; x <= maxX ; x+= squareSize){			
			g.drawLine(x, 0, x, maxY);
		}

	}

	// have two options for kibble-> a square or a frog or any pic
	// resized the image of frog to 50*50 pixel and imported in into a buffered image object
	//drew the image? if need the kibble square back, can comment out the draw image and uncomment g.fillrect
	private void displayKibble(Graphics g)
	{

		//Draw the kibble in green-Original lines
//		g.setColor(Color.GREEN);
//		int x = kibble.getKibbleX() * SnakeGame.squareSize;
//		int y = kibble.getKibbleY() * SnakeGame.squareSize;
//
//		g.fillRect(x+1, y+1, SnakeGame.squareSize-2, SnakeGame.squareSize-2);
		//g.setColor(Color.GREEN);
		try
		{
			g.setColor(Color.GREEN);
			int x = kibble.getKibbleX() * SnakeGame.squareSize;
			int y = kibble.getKibbleY() * SnakeGame.squareSize;
		image = ImageIO.read(new File("/Users/chitrakakkar/Desktop/Frog.png"));

		//int x = kibble.getKibbleX();
		//int y = kibble.getKibbleY();
		//ScaledImage = image.getScaledInstance(x*5, y*5, image.SCALE_SMOOTH);
		g.drawImage(image,x,y,null);
			//g.fillRect(x+1, y+1, SnakeGame.squareSize-2, SnakeGame.squareSize-2);
	}
		catch (IOException ex)
		{
			System.out.println("Hello");
		}
	}

	// a method to check to display the frog

//	private void displayFrog(Graphics g)
//	{
//		try
//		{
//			g.setColor(Color.black);
//			//image = ImageIO.read(new File("/Users/chitrakakkar/Desktop/Cherry.png"));
//			image = ImageIO.read(new File("/Users/chitrakakkar/Desktop/Frog.png"));
//			int x = kibble.getKibbleX();
//			int y = kibble.getKibbleY();
//			ScaledImage = image.getScaledInstance(x*10, y*10, image.SCALE_SMOOTH);
//			g.drawImage(ScaledImage,x+2,y+2,null);
//		}
//		catch (IOException ex)
//		{
//			System.out.println("Hello");
//		}
//
//	}

	private void displaySnake(Graphics g)
	{
		LinkedList<Point> coordinates = snake.segmentsToDraw();
		
		//Draw head in grey
		//changed the snakeHead color to pink
		g.setColor(snakeHeadColor);
		Point head = coordinates.pop();
		//double tt = head.getX()+head.getY();
		//String trt = String.valueOf(tt);
		g.fillRect((int)head.getX(), (int)head.getY(), SnakeGame.squareSize, SnakeGame.squareSize);
		//g.drawString(trt,SnakeGame.squareSize, SnakeGame.squareSize);
		//Draw rest of snake in black

		g.setColor(Color.PINK);
		for (Point p : coordinates)
		{
			//double t = p.getX()+p.getY();
			//String tr = String.valueOf(t);
			g.fillRect((int)p.getX(), (int)p.getY(), SnakeGame.squareSize, SnakeGame.squareSize);
			//g.drawString(tr,SnakeGame.squareSize, SnakeGame.squareSize);
		}
	}
	private void displayMazes(Graphics g)
	{
		// whatever UseMazewall returns
		if(SnakeGame.USeMazeWall)
		{
			g.setColor(Color.BLUE);
			HashMap<Integer, Integer> MazesData = new HashMap<>();
			//MazesData = mazes.CreateMaze(snake);
			MazesData= mazes.CreateMaze();
			for (Object k : MazesData.keySet()
					) {
				int x = (int) k * SnakeGame.squareSize;
				int y = MazesData.get(k) * SnakeGame.squareSize;
				System.out.println(x + ":::" + y);
				g.fill3DRect(x, y, SnakeGame.squareSize - 3, SnakeGame.squareSize - 3, true);
			}
		}

		else
		{
			snake.hitMaze = false;
			return;
		}
	}
	//instruction for the game before game
	private void displayInstructionsAndOption(Graphics g)
	{
		g.setColor(Color.blue);
		g.drawString("Game Options",100,100);
		g.drawString("Press W TO Activate Warp Wall",100,200);
		g.drawString("Press M TO Activate Maze",100,250);
		g.drawString("Press 1 - 3 to change game speed", 100, 300);
        g.drawString("Press Enter key to begin!",100,350);
        g.drawString("Press q to quit the game",100,400);
    	}
}

