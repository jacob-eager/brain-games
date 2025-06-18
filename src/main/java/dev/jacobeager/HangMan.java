package dev.jacobeager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;


/**
 * This class creates a new instance of hangman, a popular word guessing game.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class HangMan extends JFrame implements Game, ActionListener {
	
	/**
	 * An ArrayList meant to contain all of the possible words. Is populated by calling 
	 * readWordBank() which pulls from hangman.txt.
	 */
	private static ArrayList<String> possibleWords = new ArrayList<String>();
	
	/**
	 * An ArrayList that keeps track of what characters the user has guessed. Cleared 
	 * every time a new round starts.
	 */
	private ArrayList<Character> usedChars = new ArrayList<Character>();
	
	
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
	 * The number of incorrect guesses. Picture advances with it and resets every new round.
	 */
	private int incorrectGuesses = 0;
	
	/**
	 * A string that is shown to the user that represents what they have guessed so far.
	 */
	private String obscuredWord = "";
	
	// Components that interact with ActionListeners
	private JButton submitButton;
	private JTextField userInput, underscores;
	private JTextArea lettersBox;
	private JPanel topPanel, bottomPanel;
	private JLabel hangmanImage;
	
	

	/**
	 * Constructor that creates the GUI and begins the game.
	 */
	public HangMan() {
		
		// Gathers possible words in ArrayList and chooses one
		readWordBank();
		generateWord();
		
		// Builds GUI
		buildGUI();
		
	}
	
	private void buildGUI() {
		
		// Formatting frame
		this.setSize(new Dimension(450, 650));
		this.setResizable(false);
		this.setTitle("Hangman");
		this.getContentPane().setBackground(Color.WHITE);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setIconImage(Main.titleFrame.ICON.getImage());

		// Top panel
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.setBackground(Color.WHITE);
		GridBagConstraints constraints = new GridBagConstraints();
		this.add(topPanel);

		// Organizes top panel
		JPanel topCenter = new JPanel();
		topCenter.setLayout(new GridBagLayout());
		topCenter.setBackground(Color.WHITE);
		topPanel.add(topCenter);

		// Sets up hangman picture for first stage
		hangmanImage = new JLabel();
		hangmanImage.setIcon(new ImageIcon(getClass().getResource("/images/Hangman-0.png")));
		hangmanImage.setBackground(Color.WHITE);
		constraints.gridx = 0;
		constraints.gridy = 0;
		topCenter.add(hangmanImage, constraints);

		// Generates underscores to represent unguessed word
		underscores = new JTextField();
		for (int i = 0; i < currWord.length(); ++i) {
			obscuredWord += "_ ";
		}
		underscores.setText(obscuredWord);
		underscores.setHorizontalAlignment(JTextField.CENTER);
		underscores.setBackground(Color.WHITE);
		underscores.setEditable(false);
		underscores.setCaretColor(Color.WHITE);
		underscores.setPreferredSize(new Dimension(400, 100));
		underscores.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		constraints.gridy = 1;
		topCenter.add(underscores, constraints);

		// Bottom panel
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.setBackground(Color.WHITE);
		this.add(bottomPanel);

		// Labels box of used letters
		JLabel lettersUsed = new JLabel("Letters used: ");
		bottomPanel.add(lettersUsed);

		// Textbox containing all used letters
		lettersBox = new JTextArea();
		lettersBox.setPreferredSize(new Dimension(100, 100));
		lettersBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lettersBox.setEditable(false);
		lettersBox.setCaretColor(Color.WHITE);
		bottomPanel.add(lettersBox);

		// Holds submission components (input box, submit button)
		JPanel submissionHolder = new JPanel();
		submissionHolder.setBackground(Color.WHITE);
		bottomPanel.add(submissionHolder);

		// Input box
		userInput = new JTextField();
		userInput.setPreferredSize(new Dimension(36, 36));
		userInput.setHorizontalAlignment(JTextField.CENTER);
		submissionHolder.add(userInput);

		// Keybind for enter button
		Action enter = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == submitButton) {
					guess(userInput.getText());
				}
			}
		};

		// Submit button
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		submitButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
		submitButton.getActionMap().put("enter", enter);
		submissionHolder.add(submitButton);

		getRootPane().setDefaultButton(submitButton);
		this.setVisible(true);

	}
	
	/**
	 * Reads a string from the guess box. Validates it, and checks for correctness. Can end 
	 * the game if the guess is incorrect and the user is on their last guess.
	 * @param input the string read from the textbox
	 */
	
	private void guess(String input) {
		
		// Standardizes input
		input = input.toLowerCase();
		
		// Does nothing if input is invalid
		if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
			return;
		}
		else {
			// Checks if letter has already been guessed
			for (char c : usedChars) {
				if (input.charAt(0) == c) {
					return;
				}
			}
			
			// Adds letter to the list of guessed letters and clears box
			usedChars.add(input.charAt(0));
			userInput.setText("");
			
			// Updates the visual representation of the unguessed word with new letter
			if (currWord.contains(input)) {
				updateUnderscores(input.charAt(0));
			}
			else {
				// Advances to next stage, updates picture and number of incorrect guesses
				++incorrectGuesses;
				updatePicture();
				lettersBox.setText(lettersBox.getText() + " " + input.charAt(0));
				
				// Ends game after a brief pause
				if (incorrectGuesses >= 6) {
					userInput.setEditable(false);
					Timer timer = new Timer(2000, new ActionListener() {
					    public void actionPerformed(ActionEvent e) {
					    	gameOver();
					    }
					});
					timer.setRepeats(false);
					timer.start();
					
				}
			}
		}
	}

	/**
	 * Takes in a character and updates the string obscuredWord, allowing the user to 
	 * see where their correct guess is in the word. Also, if the guess fills in the 
	 * last character, wins the round.
	 * @param c the guessed character
	 */
	private void updateUnderscores(char c) {
		
		int charCounter = -1; // Starts at -1 so the first character will be 0
		
		// Builds a string taking the obscured representation of the answer and adding in character
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < obscuredWord.length(); ++i) {
			if (obscuredWord.charAt(i) != ' ') {
				++charCounter;
				if (c == currWord.charAt(charCounter)) {
					sb.append(c);
				}
				else {
					sb.append(obscuredWord.charAt(i));
				}
			}
			else {
				sb.append(obscuredWord.charAt(i));
			}
		}
		obscuredWord = sb.toString();
		underscores.setText(obscuredWord);
		
		// If all empty spaces are filled, wins the game
		if (!obscuredWord.contains("_")) {
			underscores.setText("");
			win();
		}
	}
	/**
	 * Updates the picture of the stick figure with every incorrect guess.
	 */
	private void updatePicture() {

		switch (incorrectGuesses) {

		case 1 -> hangmanImage.setIcon(new ImageIcon(getClass().getResource("/images/Hangman-1.png")));

		case 2 -> hangmanImage.setIcon(new ImageIcon(getClass().getResource("/images/Hangman-2.png")));

		case 3 -> hangmanImage.setIcon(new ImageIcon(getClass().getResource("/images/Hangman-3.png")));

		case 4 -> hangmanImage.setIcon(new ImageIcon(getClass().getResource("/images/Hangman-4.png")));

		case 5 -> hangmanImage.setIcon(new ImageIcon(getClass().getResource("/images/Hangman-5.png")));

		case 6 -> hangmanImage.setIcon(new ImageIcon(getClass().getResource("/images/Hangman-6.png")));
		
		}
	}
	
	/**
	 * Updates the score and resets fields and GUI, starting a new round.
	 */
	private void win() {
		
		// Plays sound for win
		Game.playCorrectAnswerSound();
		
		// Updates score and resets fields
		score += 1;
		incorrectGuesses = 0;
		obscuredWord = "";
		usedChars.clear();
		
		// Clears content from page
		this.remove(bottomPanel);
		this.remove(topPanel);
		
		// Generates a new word
		generateWord();

		// Resets layout to default (identical to constructor)
		buildGUI();
		
		// Repaints and revalidates
		this.repaint();
		this.revalidate();
		
		
		
	}
	
	/**
	 * Reads a guess when the user presses the submit button.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton) {
			guess(userInput.getText());
		}
		
	}
	
	/**
	 * Reads from hangman.txt to populate the ArrayList possibleWords.
	 */
	private void readWordBank() {
		
		try {
			FileInputStream fileByteStream = new FileInputStream(FileSetup.getDirectoryPath() + "hangman.txt");
			Scanner inFS = new Scanner(fileByteStream);
			
			if (!inFS.hasNext()) {
				inFS.close();
				throw new EmptyFileException();
			}
			
			while (inFS.hasNext()) {
				possibleWords.add(inFS.nextLine());
			}
			
			inFS.close();
		}
		
		catch (EmptyFileException e){
			FileSetup.validateFiles();
			e.printStackTrace();
		} 
		catch (FileNotFoundException e) {
			FileSetup.validateFiles();
			e.printStackTrace();
		}
	}
	
	/**
	 * Randomly chooses a word to be the hidden word and updates the field currWord
	 */
	private void generateWord() {
		Random r = new Random();
		currWord = possibleWords.get(r.nextInt(0,possibleWords.size()));
	}
	
	/**
	 * Signals to the user that the game is over, stopping the game and displaying 
	 * the user's score.
	 */
	@Override
	public void gameOver() {
		
		// Clears current layout
		this.remove(bottomPanel);
		this.remove(topPanel);
		
		// Resets background
		this.getContentPane().setBackground(null);
		
		// Sets layout to BorderLayout
		this.setLayout(new BorderLayout());
		
		// Displays game over screen
		this.add(new GameOverScreen(score, this), BorderLayout.CENTER);
		
		// Repaints and revalidates
		this.repaint();
		this.revalidate();
		
		// Adds score to leaderboard
		Leaderboard.addHighScore(Main.titleFrame.currUsername, score, "hangmanLeaderboard.json");
		
	}
	


}
