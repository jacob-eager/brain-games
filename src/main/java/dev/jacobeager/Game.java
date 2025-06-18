package dev.jacobeager;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
	
	
	/**
	 * Plays correct.mp3
	 */
	public static void playCorrectAnswerSound() {
		try {
			AudioInputStream gameOverSound = AudioSystem.getAudioInputStream(
					new File("src\\main\\resources\\correct.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(gameOverSound);
			clip.start();
		} 
		catch (UnsupportedAudioFileException e) {
			FileSetup.validateFiles();
			e.printStackTrace();
		} 
		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			FileSetup.validateFiles();
			e.printStackTrace();
		}
	}
	
}
