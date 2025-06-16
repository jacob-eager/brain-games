package dev.jacobeager;

/**
 * This interface establishes the two methods used by all three games.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

public interface Game {
	
	/**
	 * When the game ends, takes the score and the current user logged in and records
	 * it to the leaderboard text file. 
	 * 
	 * @param user the username to be added
	 * @param score the high score to be added
	 */
	public void addHighScore(String user, int score);
	
	/**
	 * Signals to the user that the game is over, stopping the game and displaying 
	 * the user's score.
	 */
	public void gameOver();
	
}
