package dev.jacobeager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This class is used to gather text from the leaderboard files and display them in a new GUI.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Leaderboard extends JFrame {

	public static ArrayList<HighScore> wordleScores;
	
	public static ArrayList<HighScore> hangmanScores;
	
	public static ArrayList<HighScore> quizBowlScores;
	
	
	/**
	 * Constructor that creates the GUI.
	 */
	public Leaderboard() {
		
		// Fills arrays of high scores from .json files
		fillArrays();
		
		// Formats frame
		this.setSize(new Dimension (400, 500));
		this.setResizable(false);
		this.setTitle("Leaderboard");
		this.setIconImage(Main.titleFrame.ICON.getImage());
		
		// Panel containing quiz bowl high scores
		JPanel quizBowlPanel = new JPanel();
		
		// Labels high scores
		JLabel scoreLabel1 = new JLabel("High Scores:");
		scoreLabel1.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		quizBowlPanel.add(scoreLabel1);
		
		// Adds text area to scroll pane
		JTextArea quizBowlText = new JTextArea();
		quizBowlText.setText(getScoreboardText("quiz bowl"));
		quizBowlText.setCaretColor(Color.WHITE);
		quizBowlText.setCaretPosition(0);
		quizBowlText.setEditable(false);
		
		// Makes scrollable pane
		JScrollPane quizBowlScores = new JScrollPane(quizBowlText,
			    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		quizBowlScores.setPreferredSize(new Dimension(300, 350));
		quizBowlPanel.add(quizBowlScores);

		// Panel containing hangman high scores
		JPanel hangmanPanel = new JPanel();
		
		// Labels high scores
		JLabel scoreLabel2 = new JLabel("High Scores:");
		scoreLabel2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		hangmanPanel.add(scoreLabel2);
		
		// Adds text area to scroll pane
		JTextArea hangmanText = new JTextArea();
		hangmanText.setText(getScoreboardText("hangman"));
		hangmanText.setCaretColor(Color.WHITE);
		hangmanText.setCaretPosition(0);
		hangmanText.setEditable(false);
		
		// Makes scrollable pane
		JScrollPane hangmanScores = new JScrollPane(hangmanText, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		hangmanScores.setPreferredSize(new Dimension(300, 350));
		hangmanPanel.add(hangmanScores);
		
		// Panel containing wordle scores
		JPanel wordlePanel = new JPanel();
		
		// Labels high scores
		JLabel scoreLabel3 = new JLabel("High Scores:");
		scoreLabel3.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		wordlePanel.add(scoreLabel3);
		
		// Adds text area to scroll pane
		JTextArea wordleText = new JTextArea(0,30);
		wordleText.setText(getScoreboardText("wordle"));
		wordleText.setCaretColor(Color.WHITE);
		wordleText.setCaretPosition(0);
		wordleText.setEditable(false);
		
		// Makes scrollable pane
		JScrollPane wordleScores = new JScrollPane(wordleText, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		wordleScores.setPreferredSize(new Dimension(300, 350));
		wordlePanel.add(wordleScores);
	
		// Adds the three panels to the menu tab
		JTabbedPane menuTab = new JTabbedPane();
		menuTab.setBounds(50,50,300,300);
		menuTab.add("Quiz Bowl",quizBowlPanel);
		menuTab.add("Hangman",hangmanPanel);
		menuTab.add("Wordle",wordlePanel);
		this.add(menuTab);
		
		this.setVisible(true);
	}
	
	/**
	 * Takes the data from a given text path and turns it into a string to be shown.
	 * @param filePath the path of the leaderboard file
	 * @return a text representation of the sorted leaderboard
	 */
	private static String getScoreboardText(String game) {
		
		String text = "";
		
		ArrayList<HighScore> scores;
		
		switch (game) {
		
			case ("wordle"):
				scores = wordleScores;
				break;
			case ("quiz bowl"):
				scores = quizBowlScores;
				break;
			case ("hangman"):
				scores = hangmanScores;
				break;
			default:
				scores = null;
		}
		
		for (int i = 0; i < scores.size(); ++i) {
			text += (i+1) + ". " + scores.get(i).toString() + "\n";
		}
		
		return text;
	}
	
	// Gets top 5 scores, for use in GameOverScreen
	public static String getTopScores(Game game) {
		String text = "";
		
		ArrayList<HighScore> scores;
		
		if (game instanceof Wordle) {
			scores = wordleScores;
		}
		else if (game instanceof QuizBowl) {
			scores = quizBowlScores;
		}
		else if (game instanceof HangMan) {
			scores = hangmanScores;
		}
		else {
			scores = null;
		}
		
		for (int i = 0; i < 5; ++i) {
			text += (i+1) + ". " + scores.get(i).toString() + "\n";
		}
		
		return text;
	}
	
	public static void fillArrays() {


		String[] fileNames = {"quizLeaderboard.json",
				"wordleLeaderboard.json",
				"hangmanLeaderboard.json"};

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			for (String fileName : fileNames) {
				// Creates an object for the respective leaderboard file and checks if file is empty
				File file = new File(FileSetup.getDirectoryPath() + fileName);
				if (file.length() == 0) {
					throw new EmptyFileException();
					
				}
				// Fills out each array
				switch (fileName) {
					case ("quizLeaderboard.json"):
						quizBowlScores = objectMapper.readValue(file,
								objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HighScore.class));
						quizBowlScores.sort(null);
						break;
					case ("wordleLeaderboard.json"):
						wordleScores = objectMapper.readValue(file,
								objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HighScore.class));
						wordleScores.sort(null);
						break;
					case ("hangmanLeaderboard.json"):
						hangmanScores = objectMapper.readValue(file,
								objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HighScore.class));
						hangmanScores.sort(null);
						break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EmptyFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This inner class is used to make objects which easily keep high scores and their associated users together. 
	 * 
	 * @author Jacob Eager
	 * @version 1.0
	 */

	public static class HighScore implements Comparable<HighScore> {
		
		String name;
		int score;
		
		// No-args constructor and getters/setters needed for Jackson
		public HighScore() {
			
		}
		
		public HighScore(String name, int score) {
			this.name = name;
			this.score = score;
		}
		
		public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public int getScore() {
	        return score;
	    }

	    public void setScore(int score) {
	        this.score = score;
	    }
		
		@Override
		public String toString() {
			String stringRep = name + ": " + score;
			return stringRep;
		}

		@Override
		public int compareTo(HighScore other) {
			return Integer.compare(other.score, this.score);
		}
	}
	
	/**
	 * When the game ends, takes the score and the current user logged in and records
	 * it to the leaderboard text file. 
	 * 
	 * @param user the username to be added
	 * @param score the high score to be added
	 */
	
	
	public static void addHighScore(String user, int score, String fileName) {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File file = new File(FileSetup.getDirectoryPath() + fileName);
			HighScore currScore = new HighScore(user, score);
			ArrayList<HighScore> highScores = new ArrayList<>();

			// Checks if file is empty, and if it's not, reads to array
			if (file.length() > 0) {
				highScores = objectMapper.readValue(file,
						objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, HighScore.class));
			} else {
				throw new EmptyFileException();
			}

			// Adds score and re-writes file
			highScores.add(currScore);
			objectMapper.writeValue(file, highScores);

		} catch (FileNotFoundException e) {
			FileSetup.validateFiles();
			e.printStackTrace();
		} catch (EmptyFileException e) {
			FileSetup.validateFiles();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
