package dev.jacobeager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * This class creates the main title page for the game. From here, a user can select each of the 
 * three games, as well as change their profile or view the leaderboard.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class TitleFrame extends JFrame implements ActionListener {
	
	
	/**
	 *  Icon for all frames in the app
	 */
	public final ImageIcon ICON = new ImageIcon(getClass().getResource("/images/appIcon.png"));
	
	/**
	 * Current username, kept track of here to be displayed. Also used for printing high scores.
	 * Set to Guest by default.
	 */
	protected String currUsername = LoginFrame.DEFAULT_USERNAME;
	
	// Components that use/are effected by ActionListeners
	private JButton loginButton, leaderboardButton, tutorialButton;
	private GameButton wordleButton, quizBowlButton, hangmanButton;
	private JLabel usernameLabel;
	
	
	/**
	 * Constructor which creates and formats the title screen.
	 */
	public TitleFrame() {
		
		// Background color
		Color backgroundColor = Color.LIGHT_GRAY;
		
		// Setting frame basics
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("BrainGames");
		this.setSize(new Dimension(1000,750));
		this.setResizable(false);
		this.setLayout(new GridLayout(2,1));
		this.setIconImage(ICON.getImage());
		
		// JPanel that contains everything in the top half of the screen
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.BLACK);
		topPanel.setLayout(new GridLayout(2,1));
		this.add(topPanel);
		
		// JPanel that contains username, login button, and leaderboard button
		JPanel topComponents = new JPanel();
		topComponents.setLayout(new FlowLayout(FlowLayout.TRAILING));
		topComponents.setBackground(backgroundColor);
		topPanel.add(topComponents);
		
		// Shows current username (updated when account is changed)
		usernameLabel = new JLabel();
		usernameLabel.setText(currUsername);
		topComponents.add(usernameLabel);
		
		// Login icon (pixelated profile in top right, click to log on)
		loginButton = new JButton();
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/profile.png"));
		Image image = icon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
		loginButton.setIcon(new ImageIcon(image));
		loginButton.setPreferredSize(new Dimension(36,36));
		loginButton.setFocusable(false);
		loginButton.setToolTipText("Account");
		loginButton.setContentAreaFilled(false);
		loginButton.setBorder(null);
		loginButton.addActionListener(this);
		topComponents.add(loginButton);
		
		// Leaderboard icon (click to view leaderboard)
		leaderboardButton = new JButton();
		leaderboardButton.setIcon(new ImageIcon(getClass().getResource("/images/leaderboard.png")));
		leaderboardButton.setContentAreaFilled(false);
		leaderboardButton.setFocusable(false);
		leaderboardButton.setToolTipText("Leaderboards");
		leaderboardButton.setMargin(new Insets(0, 0, 0, 0));
		leaderboardButton.setBorder(null);
		leaderboardButton.addActionListener(this);
		topComponents.add(leaderboardButton);
		
		// Tutorials icon
		tutorialButton = new JButton();
		tutorialButton.setIcon(new ImageIcon(getClass().getResource("/images/tutorials.png")));
		tutorialButton.setContentAreaFilled(false);
		tutorialButton.setFocusable(false);
		tutorialButton.setToolTipText("Tutorials");
		tutorialButton.setMargin(new Insets(0, 0, 0, 0));
		tutorialButton.setBorder(null);
		tutorialButton.addActionListener(this);
		topComponents.add(tutorialButton);
		
		// Used to adjust position of the title text
		JPanel titleLayout = new JPanel();
		titleLayout.setLayout(new FlowLayout(FlowLayout.CENTER,0,75));
		titleLayout.setBackground(backgroundColor);
		topPanel.add(titleLayout);
		
		// Title text
		JLabel logo = new JLabel();
		logo.setText("BrainGames");
		logo.setFont(new Font("Gill Sans Ultra Bold", Font.BOLD, 70));
		logo.setForeground(Color.DARK_GRAY);
		titleLayout.add(logo);
		
		// JPanel for formatting the bottom half of the screen
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(backgroundColor);
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER,125,80));
		this.add(bottomPanel);
		
		// Button to take user to each game
		wordleButton = new GameButton("Wordle", new Color(102, 204, 0), Color.WHITE);
		wordleButton.addActionListener(this);
		bottomPanel.add(wordleButton);
		
		quizBowlButton = new GameButton("Quiz Bowl", new Color(255, 193, 7), Color.BLACK);
		quizBowlButton.addActionListener(this);
		bottomPanel.add(quizBowlButton);
		
		hangmanButton = new GameButton("Hangman", new Color(33, 150, 243), Color.WHITE);
		hangmanButton.addActionListener(this);
		bottomPanel.add(hangmanButton);
		
		this.setVisible(true);
		
	}
	
	// Each button opens its respective page.
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			new LoginFrame();
		}
		if (e.getSource() == leaderboardButton) {
			new Leaderboard();
		}
		if (e.getSource() == tutorialButton) {
			new Tutorials();
		}
		if (e.getSource() == wordleButton) {
			new Wordle();
		}
		if (e.getSource() == quizBowlButton) {
			new QuizBowl();
		}
		if (e.getSource() == hangmanButton) {
			new HangMan();
		}
		
	}
	
	/**
	 * Used by the LoginFrame class to change the username here.
	 * @param username new username
	 */
	public void setUsername(String username) {
		usernameLabel.setText(username);
		currUsername = username;
	}

	
	/**
	 * This inner class is used for declaring the three game buttons on the title screen.
	 * 
	 * @author Jacob Eager
	 * @version 1.0
	 */

	private class GameButton extends JButton {

		/**
		 * This constructor takes in the desired text and colors and creates a JButton
		 * with them.
		 * 
		 * @param text        the text in the button
		 * @param buttonColor the color of the button
		 * @param textColor   the color of the text
		 */
		public GameButton(String text, Color buttonColor, Color textColor) {

			// Button formatting
			setText(text);
			setBackground(buttonColor);
			setFont(new Font("Helvetica", Font.BOLD, 20));
			setForeground(textColor);
			setBorder(BorderFactory.createLineBorder(Color.GRAY, 5, true));
			setFocusable(false);
			setPreferredSize(new Dimension(150, 150));

		}

	}
}
