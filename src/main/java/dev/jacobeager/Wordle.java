package dev.jacobeager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;


/**
 * This class creates a new instance of Wordle, a popular word guessing game.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Wordle extends JFrame implements Game, ActionListener {
	
	
	/**
	 *  Word length stored as a final int
	 */
	private static final int WORD_LENGTH = 5;
	
	/**
	 * An ArrayList meant to contain all of the possible words. Is populated by calling 
	 * readWordBank() which pulls from wordle.txt
	 */
	private static ArrayList<String> possibleWords = new ArrayList<String>();
	
	/**
	 * The current hidden word to be guessed in the game. Chosen randomly from possibleWords 
	 * when generateWord is called.
	 */
	private String currWord;
	
	/**
	 * The current score. Increases by one for every word solved.
	 */
	private int score = 0;
	
	/**
	 * The number of guesses used. Maxes out at 6 and resets every new game.
	 */
	private int numGuesses = 0;
	
	// Array of text boxes, used to populate the letter grid
	private JTextField[] guessBoxes = new JTextField[30];
	
	// Components that have an ActionListener
	private JButton guessButton;
	private JPanel centerLock;

	/**
	 * Constructor that creates the GUI and begins the game.
	 */
	public Wordle() {

		// Fills in the array of possible words and chooses one
		readWordBank();
		generateWord();

		// Formats frame
		buildGUI();

	}
	
	private void buildGUI() {
		// Formats frame
		this.setSize(new Dimension(500, 600));
		this.setTitle("Wordle");
		this.setIconImage(Main.titleFrame.ICON.getImage());
		this.setLayout(new FlowLayout(FlowLayout.CENTER));

		// Panel to lock the game in the center of the screen
		centerLock = new JPanel();
		centerLock.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		this.add(centerLock);

		// Grid of letter boxes
		JPanel inputGrid = new JPanel();
		inputGrid.setLayout(new GridLayout(6, 5, 5, 5));
		inputGrid.setPreferredSize(new Dimension(300, 400));
		inputGrid.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.DARK_GRAY));
		constraints.gridx = 0;
		constraints.gridy = 0;
		centerLock.add(inputGrid, constraints);

		// Populates grid
		for (int i = 0; i < 30; ++i) {
			guessBoxes[i] = new JTextField();
			guessBoxes[i].setHorizontalAlignment(JTextField.CENTER);
			guessBoxes[i].setFont(new Font("Arial", Font.BOLD, 20));
			inputGrid.add(guessBoxes[i]);
			if (i > 4) {
				guessBoxes[i].setEditable(false);
			}
		}

		// Keybind for enter button
		Action enter = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				boolean isValid = validateGuess((numGuesses * 5), (numGuesses * 5) + 5);

				if (isValid) {
					advance();
				}
			}
		};

		// Button to guess
		guessButton = new JButton("Guess");
		guessButton.setPreferredSize(new Dimension(150, 75));
		guessButton.setFocusable(false);
		guessButton.setFont(new Font("Segoe UI", Font.BOLD, 30));
		guessButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		guessButton.setBackground(Color.decode("#B0C4DE"));
		guessButton.addActionListener(this);
		guessButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
		guessButton.getActionMap().put("enter", enter);
		constraints.gridy = 1;
		constraints.insets = new Insets(10, 0, 0, 0);
		centerLock.add(guessButton, constraints);

		this.setVisible(true);

		getRootPane().setDefaultButton(guessButton);
	}
	
	/**
	 * Reads from wordle.txt to populate the ArrayList possibleWords.
	 */
	private void readWordBank() {

		try {
			FileInputStream fileByteStream = new FileInputStream(FileSetup.getDirectoryPath() + "wordle.txt");
			Scanner inFS = new Scanner(fileByteStream);

			// Checks if file is empty
			if (!inFS.hasNext()) {
				inFS.close();
				throw new EmptyFileException();
			}

			// Adds words to string array
			while (inFS.hasNext()) {
				possibleWords.add(inFS.nextLine());
			}

			inFS.close();
		}

		catch (EmptyFileException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Randomly chooses a word to be the hidden word and updates the field currWord.
	 */
	private void generateWord() {
		
		Random r = new Random();
		currWord = possibleWords.get(r.nextInt(0, possibleWords.size()));
		
	}

	/**
	 * Is given a minimum index and a maximum index, uses these to search through the
	 * input boxes. Checks if the user's guess is valid and returns a boolean result.
	 * @param min the minimum index to search
	 * @param max the maximum index to search
	 * @return a boolean representing if the guess is valid or not
	 */
	private boolean validateGuess(int min, int max) {
		
		// Checks if the input is one character and a letter
		for (int i = min; i < max; ++i) {
			if (guessBoxes[i].getText().length() != 1) {
				return false;
			}
			else if (!Character.isLetter(guessBoxes[i].getText().charAt(0))) {
				return false;
			}
		}
		
		// If every letter passes both tests, returns true
		return true;
	}
	
	/**
	 * When the game ends, takes the score and the current user logged in and records
	 * it to the leaderboard text file. 
	 * 
	 * @param user the username to be added
	 * @param score the high score to be added
	 */
	@Override
	public void addHighScore(String user, int score) {
		try {
			if (!new File(FileSetup.getDirectoryPath() + "wordleLeaderboard.txt").exists()) {
				throw new FileNotFoundException();
			}
			FileWriter outputStream = new FileWriter(FileSetup.getDirectoryPath() + "wordleLeaderboard.txt", true);
			BufferedWriter outFS = new BufferedWriter(outputStream);
			outFS.write(user + "\n");
			outFS.write(score + "\n");
			outFS.close();
			outputStream.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			FileSetup.validateFiles();
			addHighScore(user, score);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * "Advances" in the game. Disables current row, enables next row, colors in boxes, and
	 * checks for a win, ending the game if so. 
	 */
	private void advance() {
		String guess = "";
		
		if (numGuesses < 5) {
			// Disables previous row and colors in boxes
			for (int i = 0; i < 5; ++i) {
				guess += guessBoxes[i + (numGuesses * WORD_LENGTH)].getText().toLowerCase();
				if (currWord.contains(Character.toString(guess.charAt(i)))) {
					if (guess.charAt(i) == currWord.charAt(i)) {
						guessBoxes[i + (numGuesses * WORD_LENGTH)].setEditable(false);
						guessBoxes[i + (numGuesses * WORD_LENGTH)].setBackground(Color.GREEN);
						guessBoxes[i + (numGuesses * WORD_LENGTH)].setCaretColor(Color.GREEN);
					} else {
						guessBoxes[i + (numGuesses * WORD_LENGTH)].setEditable(false);
						guessBoxes[i + (numGuesses * WORD_LENGTH)].setBackground(Color.YELLOW);
						guessBoxes[i + (numGuesses * WORD_LENGTH)].setCaretColor(Color.YELLOW);
					}
				} else {
					guessBoxes[i + (numGuesses * WORD_LENGTH)].setEditable(false);
					guessBoxes[i + (numGuesses * WORD_LENGTH)].setBackground(Color.GRAY);
					guessBoxes[i + (numGuesses * WORD_LENGTH)].setCaretColor(Color.GRAY);
				}
			}
		} else {
			for (int i = 25; i < 30; ++i) {
				guess += guessBoxes[i].getText().toLowerCase();
			}
			if (guess.equals(currWord)) {
				for (int i = 25; i < 30; ++i) {
					guessBoxes[i].setBackground(Color.GREEN);
				}
				Timer timer = new Timer(2000, new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				    	++score;
						restartGame();
				    }
				});
				timer.setRepeats(false);
				timer.start();
				
			}
			else {
				for (int i = 25; i < 30; ++i) {
					guessBoxes[i].setBackground(Color.RED);
				}
				Timer timer = new Timer(2000, new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				    	gameOver();
				    }
				});
				timer.setRepeats(false);
				timer.start();
			}
			
		}
		// Wins game if word is guessed
		if (guess.equals(currWord)) {
			// Waits a couple seconds before resetting to show success
			Timer timer = new Timer(2000, new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	++score;
					restartGame();
			    }
			});
			timer.setRepeats(false);
			timer.start();
		}
		// Advances to the next row if game is not won
		else {
			for (int i = 5 * (numGuesses + 1); i < (5 * (numGuesses + 2)); ++i) {
				guessBoxes[i].setEditable(true);
			}
			++numGuesses;
		}
	}
	
	/**
	 * Listens for input from the guess button, with varying behavior depending on how many 
	 * guesses are used. On the sixth guess, can win or lose the game.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == guessButton) {
			
			boolean isValid = validateGuess((numGuesses * 5), (numGuesses * 5) + 5);
			
			if (isValid) {
				advance();
			}
		}
		
	}
	
	/**
	 * Resets everything back to the default state, keeping the increase in score and 
	 * generating a new word.
	 */
	public void restartGame() {
		
		// Picks a new word
		generateWord();
		
		// Resets number of guesses
		numGuesses = 0;
		
		// Removes old content
		this.remove(centerLock);
		
		// Re-makes GUI
		buildGUI();
		
		// Repaints and revalidates
		this.repaint();
		this.revalidate();
		
		this.setVisible(true);
	}

	/**
	 * Signals to the user that the game is over, stopping the game and displaying 
	 * the user's score.
	 */
	@Override
	public void gameOver() {
		
		// Clears current content
		this.remove(centerLock);
		
		// Sets layout to BorderLayout
		this.setLayout(new BorderLayout());
		
		// Adds game over screen
		this.add(new GameOverScreen(score, this), BorderLayout.CENTER);
	
		// Repaints and revalidates
		this.repaint();
		this.revalidate();
		
		// Adds high score to leaderboard document
		addHighScore(Main.titleFrame.currUsername, score);
		
	}

}
