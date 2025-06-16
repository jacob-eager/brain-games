package dev.jacobeager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class creates a new instance of a quiz game.
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
	public class QuizBowl extends JFrame implements Game {
	
	/**
	 * The current score. Increases by one for every question solved.
	 */
	private int score = 0;
	
	/**
	 * An ArrayList that stores the text file data for all multiple choice questions.
	 */
	private static ArrayList<MultipleChoiceQuestion> multQuestions = new ArrayList<MultipleChoiceQuestion>();
	
	/**
	 * An ArrayList that stores the text file data for all text questions.
	 */
	private static ArrayList<TextQuestion> textQuestions = new ArrayList<TextQuestion>();
	
	// Declared as fields because their contents change depending on question type
	private JPanel upperPanel, lowerPanel;

	/**
	 * Constructor that creates the GUI and begins the game.
	 */
	public QuizBowl() {

		// Formatting the frame
		this.setSize(new Dimension(1250, 700));
		this.setResizable(false);
		this.setTitle("Quiz Bowl");
		this.setIconImage(Main.titleFrame.ICON.getImage());
		this.setLayout(new GridLayout(2, 1));

		// Top half of the background
		upperPanel = new JPanel();
		upperPanel.setBackground(Color.decode("#46178f"));
		this.add(upperPanel);

		// Bottom half of the background
		lowerPanel = new JPanel();
		lowerPanel.setBackground(Color.decode("#46178f"));
		this.add(lowerPanel);

		// Generates a question and starts the game
		generateQuestion();

		this.setVisible(true);

	}
	
	/**
	 * Randomly selects either a multiple choice question or a text question. 
	 * 3/4 chance of multiple choice question 
	 * 1/4 chance of text question
	 */
	private void generateQuestion() {
		
		Random r = new Random();
		
		Question q = (r.nextInt(4) < 3) ? getMultipleChoiceQuestion() : getTextQuestion();
		
		q.displayQuestion(this);

	}
	
	/**
	 * Pulls a random multiple choice question from the ArrayList.
	 * @return the question selected
	 */
	private MultipleChoiceQuestion getMultipleChoiceQuestion() {
		
		Random r = new Random();

		// Checks if the array of text questions is filled, and if it's empty, fills it
		if (multQuestions.size() == 0) {
			try {

				ObjectMapper objectMapper = new ObjectMapper();
				File file = new File(FileSetup.getDirectoryPath() + "multipleChoiceQuestions.json");

				if (file.length() > 0) {
					multQuestions = objectMapper.readValue(file,
							objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, 
									MultipleChoiceQuestion.class));
				} 
				else {
					throw new EmptyFileException();
				}

			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			catch (EmptyFileException e) {
				e.printStackTrace();
				return null;
			}
			catch (StreamReadException e) {
				e.printStackTrace();
				return null;
			} 
			catch (DatabindException e) {
				e.printStackTrace();
				return null;
			} 
			catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		// Selects a random question from the ArrayList
		return multQuestions.get(r.nextInt(0, multQuestions.size()));
		
		
	}
	/**
	 * Pulls a random text question from the ArrayList.
	 * @return the question selected
	 */
	private TextQuestion getTextQuestion() {
		
		Random r = new Random();

		// Checks if the array of text questions is filled, and if it's empty, fills it
		if (textQuestions.size() == 0) {
			try {

				ObjectMapper objectMapper = new ObjectMapper();
				File file = new File(FileSetup.getDirectoryPath() + "textQuestions.json");

				if (file.length() > 0) {
					textQuestions = objectMapper.readValue(file,
							objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, TextQuestion.class));
				} 
				else {
					throw new EmptyFileException();
				}

			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			catch (EmptyFileException e) {
				e.printStackTrace();
				return null;
			}
			catch (StreamReadException e) {
				e.printStackTrace();
				return null;
			} 
			catch (DatabindException e) {
				e.printStackTrace();
				return null;
			} 
			catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		// Selects a random question from the ArrayList
		return textQuestions.get(r.nextInt(0, textQuestions.size()));
		
	}
	
	/**
	 * Signals to the user that the game is over, stopping the game and displaying 
	 * the user's score.
	 */
	@Override
	public void gameOver() {
		
		// Removes previous formatting
		this.remove(upperPanel);
		this.remove(lowerPanel);
		
		// Centers
		this.setLayout(new BorderLayout());
		
		// Adds game over screen
		this.add(new GameOverScreen(score, this), BorderLayout.CENTER);
		
		// Repaints and revalidates
		this.repaint();
		this.revalidate();
		
		// Adds high score to .txt file
		Leaderboard.addHighScore(Main.titleFrame.currUsername, score, "quizLeaderboard.json");
	}
	

	
	
	/**
	 * Stores a text question, and its answer.
	 * @author Jacob Eager
	 * @version 1.0
	 */
	public static class TextQuestion implements Question {
		
		String question;
		String answer;
		
		public TextQuestion() {
			
		}
		
		public TextQuestion(String question, String answer) {
			this.question = question;
			this.answer = answer;
		}
		
		public String getQuestion() {
			return question;
		}
		
		public void setQuestion(String question) {
			this.question = question;
		}
		
		public String getAnswer() {
			return answer;
		}
		
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		
		@Override
		public void displayQuestion(QuizBowl frame) {
			
			// Clears and reformats upper panel
			frame.upperPanel.removeAll();
			frame.upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,100));
			
			// Displays question
			JLabel questionLabel = new JLabel(question);
			questionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
			questionLabel.setForeground(Color.WHITE);
			frame.upperPanel.add(questionLabel);
			
			// Repaints and revalidates upper panel
			frame.upperPanel.revalidate();
			frame.upperPanel.repaint();
			
			// Clears and reformats lower panel
			frame.lowerPanel.removeAll();
			frame.lowerPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,50));

			// Textbox for answer
			JTextField answerBox = new JTextField();
			answerBox.setFont(new Font("Arial", Font.PLAIN, 18));
			answerBox.setPreferredSize(new Dimension(600,100));
			frame.lowerPanel.add(answerBox);
			
			// Keybind for enter button
			Action enter = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					if (answerBox.getText().equalsIgnoreCase(answer)) {
						frame.score++;
						frame.generateQuestion();
					} 
					else {
						frame.gameOver();
					}
				}
			};
			
			// Enter button
			JButton enterButton = new JButton("Enter");
			enterButton.setPreferredSize(new Dimension(300,100));
			enterButton.setFont(new Font("Arial", Font.BOLD, 20));
			enterButton.setBackground(Color.decode("#45a3e5"));
			enterButton.setForeground(Color.WHITE);
			enterButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
			enterButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (answerBox.getText().equalsIgnoreCase(answer)) {
						frame.score++;
						frame.generateQuestion();
					} 
					else {
						frame.gameOver();
					}
				}
			});
			enterButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
			enterButton.getActionMap().put("enter", enter);
			frame.lowerPanel.add(enterButton);
			frame.getRootPane().setDefaultButton(enterButton);
			
			// Repaints and revalidates lower panel
			frame.lowerPanel.revalidate();
			frame.lowerPanel.repaint();

			
		}
	}
	
	/**
	 * Stores a multiple choice question, its possible answers, and the correct answer.
	 * @author Jacob Eager
	 * @version 1.0
	 */
	public static class MultipleChoiceQuestion implements Question {

		String question;
		
		String[] options;
		
		char answer;
		
		public MultipleChoiceQuestion() {
			
		}
		
		public String getQuestion() {
			return question;
		}
		
		public void setQuestion(String question) {
			this.question = question;
		}
		
		public String[] getOptions() {
			return options;
		}
		
		public void setOptions(String[] options) {
			this.options = options;
		}
		
		public char getAnswer() {
			return answer;
		}
		
		public void setAnswer(char answer) {
			this.answer = answer;
		}
		
		@Override
		public void displayQuestion(QuizBowl frame) {
			
			char[] letters = new char[] {'A', 'B', 'C', 'D'};
			
			String optionsText = "";
			
			// Sets options to a string to put in a text box
			for (int i = 0; i < options.length; ++i) {
				optionsText += letters[i] + ". " + options[i] + "\n";
			}
			
			// Clears former contents and sets layout
			frame.upperPanel.removeAll();
			frame.upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,50));
			
			// JPanel for containing question and answer
			JPanel qAndA = new JPanel();
			qAndA.setLayout(new GridBagLayout());
			qAndA.setBackground(Color.decode("#46178f"));
			GridBagConstraints constraints = new GridBagConstraints();
			frame.upperPanel.add(qAndA);
			
			// Displays question
			JLabel questionLabel = new JLabel(question);
			questionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
			questionLabel.setForeground(Color.WHITE);
			constraints.gridx = 0;
			constraints.gridy = 0;
			qAndA.add(questionLabel, constraints);
			
			// Displays answer
			JTextArea answersLabel = new JTextArea(optionsText);
			answersLabel.setFont(new Font("Arial", Font.PLAIN, 20));
			answersLabel.setForeground(Color.WHITE);
			answersLabel.setEditable(false);
			answersLabel.setOpaque(false);
			constraints.gridy = 1;
			qAndA.add(answersLabel, constraints);
			
			// Revalidates/repaints the upper panel (formatting gets wonky if this doesn't happen)
			frame.upperPanel.revalidate();
			frame.upperPanel.repaint();
			
			// Clears former contents and sets layout
			frame.lowerPanel.removeAll();
			frame.lowerPanel.setLayout(new GridLayout(2, 2));
			
			// Adds the four buttons to the grid
			for (char option : letters) {
				
				// Formats button
			    JButton button = new JButton(String.valueOf(option));
			    button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
			    button.setFont(new Font("Arial", Font.BOLD, 50));
			    button.setForeground(Color.WHITE);
			    
			    // Sets color of button depending on letter
				switch (option) {

				case 'A' -> button.setBackground(Color.decode("#eb21b3c"));

				case 'B' -> button.setBackground(Color.decode("#1368ce"));

				case 'C' -> button.setBackground(Color.decode("#26890c"));

				case 'D' -> button.setBackground(Color.decode("#ffa602"));

				}
			    
				// Adds action listener and logic
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (option == answer) {
							++frame.score;
							frame.generateQuestion(); // Continues game
						} else {
							frame.gameOver(); // Ends game
						}
					}
				});
				frame.lowerPanel.add(button);
			}
			// Revalidates and repaints lower panel
			frame.lowerPanel.revalidate();
			frame.lowerPanel.repaint();
			
		}
	}

}
