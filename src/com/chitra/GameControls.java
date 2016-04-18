package com.chitra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameControls extends JPanel implements KeyListener
{

	public void keyPressed(KeyEvent ev)
	{
		//keyPressed events are for catching events like function keys, enter, arrow keys
		//We want to listen for arrow keys to move snake
		//Has to id if user pressed arrow key, and if so, send info to Snake object

		//is game running? No? tell the game to draw grid, start, etc.
		
		//Get the component which generated this event
		//Hopefully, a DrawSnakeGamePanel object.

		DrawSnakeGamePanel panel = (DrawSnakeGamePanel)ev.getComponent();
		int KeyNumberPressed = ev.getKeyCode();
		//setting the listener to listen to the key pressed before the game instruction
		if (SnakeGame.getGameStage() == SnakeGame.BEFORE_GAME)
		{
			if(KeyNumberPressed == KeyEvent.VK_W  )
			{
				String W = "w";
				SnakeGame.WarpWallOnOrOff(W);
			}
			if(KeyNumberPressed == KeyEvent.VK_M)
			{
				String M = "m";
				SnakeGame.MazeWallOffOROn(M);
			}
			if(KeyNumberPressed == KeyEvent.VK_1)
			{
				SnakeGame.setGameSpeed(SnakeGame.Slower_Option_for_SpeedChange);

			}
			if(KeyNumberPressed == KeyEvent.VK_2)
			{
				SnakeGame.setGameSpeed(SnakeGame.Default_Speed);
			}
			if(KeyNumberPressed == KeyEvent.VK_3)
			{
				SnakeGame.setGameSpeed(SnakeGame.Faster_Option_for_SpeedChange);
			}
			//start the game
			if(KeyNumberPressed == KeyEvent.VK_ENTER)
			{
				SnakeGame.setGameStage(SnakeGame.DURING_GAME);
				SnakeGame.newGame();
				return;
			}
		}
		//resetting everything once the game is over
		if (SnakeGame.getGameStage() == SnakeGame.GAME_OVER)
		{
			Score.resetScore();
			//Need to start the timer and start the game again
			SnakeGame.newGame();
			SnakeGame.setGameStage(SnakeGame.DURING_GAME);
			panel.repaint();
			return;
		}
	}
	@Override
	public void keyReleased(KeyEvent ev)
	{
		//We don't care about keyReleased events, but are required to implement this method anyway.		
	}
	// This performs according to the key typed on keyboard
	@Override
	public void keyTyped(KeyEvent ev)
	{
		//keyTyped events are for user typing letters on the keyboard, anything that makes a character display on the screen
		char keyPressed = ev.getKeyChar();
		char q = 'q'; char Q ='Q';
		if( keyPressed == q || keyPressed==Q)
		{
			System.exit(0);    //quit if user presses the q key.
		}

	}

}
