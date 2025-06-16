package dev.jacobeager;

/**
 * This interface establishes the two methods used by all three games.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

public interface Game {
	
	/**
	 * Signals to the user that the game is over, stopping the game and displaying 
	 * the user's score.
	 */
	public void gameOver();
	
}
