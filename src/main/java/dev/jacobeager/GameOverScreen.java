package dev.jacobeager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * This class creates a JPanel that contains all of the information in the game over screen.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class GameOverScreen extends JPanel implements ActionListener {

	private JButton playAgain;
	
	private Game currGame;
	
	/**
	 * Displays game over text and the user's score.
	 * @param score
	 */
	public GameOverScreen(int score, Game game) {
		
		// Sets current game for play again button
		currGame = game;
		
		// Sets layout and creates constraints
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		// Game over text
		JLabel gameOver = new JLabel("Game over!");
		gameOver.setFont(new Font("Impact", Font.BOLD, 50));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10,10,10,10);
		this.add(gameOver, constraints);
		
		// Displays score
		JLabel yourScore = new JLabel("Your score: " + score);
		yourScore.setFont(new Font("Arial", Font.BOLD, 20));
		constraints.gridy = 1;
		this.add(yourScore, constraints);
		
		// Play again button
		playAgain = new JButton("Play again");
		playAgain.addActionListener(this);
		playAgain.setFocusable(false);
		playAgain.setFont(new Font("Arial", Font.BOLD, 12));
		playAgain.setPreferredSize(new Dimension(90, 50));
		playAgain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		playAgain.setBackground(Color.decode("#B0C4DE"));
		constraints.gridy = 2;
		this.add(playAgain, constraints);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Creates a new game and closes the old one
		if (e.getSource() == playAgain) {

			if (currGame instanceof QuizBowl) {
				new QuizBowl();
				((QuizBowl) currGame).dispose();
			} else if (currGame instanceof Wordle) {
				new Wordle();
				((Wordle) currGame).dispose();
			} else if (currGame instanceof HangMan) {
				new HangMan();
				((HangMan) currGame).dispose();
			}
		}
	}
}
