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

	/**
	 * Constructor that creates the GUI.
	 */
	public Leaderboard() {
		
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
		quizBowlText.setText(getScoreboardText(FileSetup.getDirectoryPath() + "quizLeaderboard.json"));
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
		hangmanText.setText(getScoreboardText(FileSetup.getDirectoryPath() + "hangmanLeaderboard.json"));
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
		wordleText.setText(getScoreboardText(FileSetup.getDirectoryPath() + "wordleLeaderboard.json"));
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
	private String getScoreboardText(String filePath) {
		
		// ArrayList for HighScore objects (contain user and score)
		ArrayList<HighScore> scores;
		
		String text = "";
		
		File file = new File(filePath);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		try {
			// Checks if leaderboard file is empty
			if (file.length() == 0) {
				throw new EmptyFileException();
			}
			scores = objectMapper.readValue(file, objectMapper.getTypeFactory()
					.constructCollectionType(ArrayList.class, HighScore.class));
			
			text = sortScores(scores);
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			text = "File not found!";
		}
		catch (EmptyFileException e) {
			e.printStackTrace();
			text = "File is empty!";
		}
		catch (IOException e) {
			e.printStackTrace();
			text = "IO Exception!";
		} 
		
		return text;
	}
	
	/**
	 * Sorts HighScores from highest to lowest.
	 * @param scores list of scores and users
	 * @return text of sorted scores and users
	 */
	private String sortScores (ArrayList<HighScore> scores) {
		String text = "";
		scores.sort(null);
		
		for (int i = 0; i < scores.size(); ++i) {
			text += (i+1) + ". " + scores.get(i).toString() + "\n";
		}
		return text;
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
		
		public HighScore() {
			// No-args constructor needed for Jackson
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
}
