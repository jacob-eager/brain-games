package dev.jacobeager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.*;



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
		quizBowlText.setText(getScoreboardText(FileSetup.getDirectoryPath() + "quizLeaderboard.txt"));
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
		hangmanText.setText(getScoreboardText(FileSetup.getDirectoryPath() + "hangmanLeaderboard.txt"));
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
		wordleText.setText(getScoreboardText(FileSetup.getDirectoryPath() + "wordleLeaderboard.txt"));
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
		ArrayList<HighScore> scores = new ArrayList<HighScore>();
		
		String user;
		String scoreNum;
		
		String text = "";
		
		try {
			FileInputStream fileByteStream = new FileInputStream(filePath);
			Scanner inFS = new Scanner(fileByteStream);
			
			// Checks if leaderboard file is empty
			if (!inFS.hasNextLine()) {
				inFS.close();
				throw new EmptyFileException();
			}
			
			while (inFS.hasNextLine()) {
				user = inFS.nextLine().trim();
				// Checks for valid username
				if (LoginFrame.validateUsername(user)) {
					if (inFS.hasNextLine()) {
						scoreNum = inFS.nextLine().trim();
						
						// Checks for valid score and adds both to array
						if (Pattern.matches("\\d{1,}", scoreNum)) {
							scores.add(new HighScore(user,Integer.parseInt(scoreNum)));
							text = sortScores(scores);
						}
						else {
							inFS.close();
							// Throws exception if no score
							throw new InvalidFormatException();
						}
					}
				}
				else {
					inFS.close();
					// Throws exception if username is invalid
					throw new InvalidFormatException();
				}
			}
			
			inFS.close();
		}
		catch (EmptyFileException e) {
			System.out.println(filePath + " was empty!");
			FileSetup.validateFiles();
			return getScoreboardText(filePath);
		}
		catch (InvalidFormatException e) {
			text = "Leaderboard file is incorrectly formatted";
		}
		catch (FileNotFoundException e) {
			System.out.println(filePath + " was not found!");
			FileSetup.validateFiles();
			return getScoreboardText(filePath);
		}
		catch (Exception e) {
			text = "Unknown error occurred!";
			e.printStackTrace();
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
	 * This record is used to make objects which easily keep high scores and their associated users together. 
	 * 
	 * @author Jacob Eager
	 * @version 1.0
	 */

	private record HighScore(String user, int score) implements Comparable<HighScore> {
		
		@Override
		public String toString() {
			String stringRep = user + ": " + score;
			return stringRep;
		}

		@Override
		public int compareTo(HighScore other) {
			return Integer.compare(other.score, this.score);
		}

	}
}
