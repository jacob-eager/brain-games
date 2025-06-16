package dev.jacobeager;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class Tutorials extends JFrame {

	public Tutorials() {
		
		// Formats frame
		this.setSize(new Dimension(400, 500));
		this.setResizable(false);
		this.setTitle("Tutorials");
		this.setIconImage(Main.titleFrame.ICON.getImage());

		// Panel containing quiz bowl high scores
		JPanel quizBowlPanel = new JPanel();

		// Labels high scores
		JLabel scoreLabel1 = new JLabel("How to play:");
		scoreLabel1.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		quizBowlPanel.add(scoreLabel1);

		// Adds text area
		JTextPane quizBowlText = new JTextPane();
		quizBowlText.setContentType("text/html");
		quizBowlText.setText("<html><body><p><font face = \"Comic Sans MS\" size = \"4\">There are two "
				+ "types of questions: multiple choice questions and text questions.<br/>"
				+ "For <b>multiple choice questions</b>, select the letter A, B, C, or D "
				+ "that best answers the question.<br/>"
				+ "For <b>text questions</b>, type in the answer to the"
				+ " given question (not case-sensitive).</font></html></body></p>");
		quizBowlText.setCaretColor(this.getBackground());
		quizBowlText.setBackground(this.getBackground());
		quizBowlText.setPreferredSize(new Dimension(300, 350));
		quizBowlText.setEditable(false);
		
		quizBowlPanel.add(quizBowlText);
		

		// Panel containing hangman high scores
		JPanel hangmanPanel = new JPanel();

		// Labels high scores
		JLabel scoreLabel2 = new JLabel("How to play:");
		scoreLabel2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		hangmanPanel.add(scoreLabel2);

		// Adds text area to scroll pane
		JTextPane hangmanText = new JTextPane();
		hangmanText.setContentType("text/html");
		hangmanText.setText("<html><body><p><font face = \"Comic Sans MS\" size = \"4\">Guess the "
				+ "hidden word in 6 tries.<br/>"
				+ "Each guess must be a valid letter.<br/>"
				+ "The blank spaces in the middle will fill in with each correct guess.<br/>"
				+ "The stick-figure in the middle will update with each incorrect guess."
				+ "</font></html></body></p>");
		hangmanText.setCaretColor(this.getBackground());
		hangmanText.setBackground(this.getBackground());
		hangmanText.setPreferredSize(new Dimension(300, 350));
		hangmanText.setEditable(false);

		hangmanPanel.add(hangmanText);
		
		
		// Panel containing wordle scores
		JPanel wordlePanel = new JPanel();

		// Labels high scores
		JLabel scoreLabel3 = new JLabel("How to play:");
		scoreLabel3.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		wordlePanel.add(scoreLabel3);

		// Adds text area to scroll pane
		JTextPane wordleText = new JTextPane();
		wordleText.setContentType("text/html");
		wordleText.setText("<html><body><p><font face = \"Comic Sans MS\" size = \"4\">Guess the "
				+ "Wordle in 6 tries.<br/>"
				+ "Each guess must be a valid 5-letter word.<br/>"
				+ "The color of the tiles will change to show how close your guess was to the word.<br/>"
				+ "If a letter is <b>green</b>, it is both in the word and in the correct spot.<br/>"
				+ "If a letter is <b>yellow</b>, it is in the word but in the wrong spot.<br/>"
				+ "If a letter is <b>gray</b>, it is not in the word in any spot."
				+ "</font></html></body></p>");
		wordleText.setCaretColor(this.getBackground());
		wordleText.setBackground(this.getBackground());
		wordleText.setPreferredSize(new Dimension(300, 350));
		wordleText.setEditable(false);

		wordlePanel.add(wordleText);

		// Adds the three panels to the menu tab
		JTabbedPane menuTab = new JTabbedPane();
		menuTab.setBounds(50, 50, 300, 300);
		menuTab.add("Wordle", wordlePanel);
		menuTab.add("Quiz Bowl", quizBowlPanel);
		menuTab.add("Hangman", hangmanPanel);
		this.add(menuTab);

		this.setVisible(true);
	}
}
