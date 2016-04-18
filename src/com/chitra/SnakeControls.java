package com.chitra;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by chitra. Handles key presses that affect the snake.
 *
 * This class handles the movement of the snake basis the key pressed
 * Inherits from KeyListener interface
 */
public class SnakeControls implements KeyListener
{

    Snake snake;

    SnakeControls(Snake s){
        this.snake = s;
    }

    public SnakeControls() {
        super();
    }


    // overrides the default method to customize it as per the game
    //can be used according to the need-> Key event has list of all keys on the keyboard
    @Override
    public void keyPressed(KeyEvent ev) {

        if (ev.getKeyCode() == KeyEvent.VK_DOWN) {
            snake.snakeDown(); // calls method from snake class to move it down
        }
        if (ev.getKeyCode() == KeyEvent.VK_UP) {
            snake.snakeUp();
        }
        if (ev.getKeyCode() == KeyEvent.VK_LEFT) {
            snake.snakeLeft();
        }
        if (ev.getKeyCode() == KeyEvent.VK_RIGHT) {
            snake.snakeRight();
        }
    }
        // not used in this game but has to define as a part of the interface
    @Override
    public void keyTyped(KeyEvent ev) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
