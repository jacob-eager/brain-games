package dev.jacobeager;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * This class is used to log an existing user in or create a new user.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener {
	
	// Username and password strings for possible storage/validation later
	protected static final String DEFAULT_USERNAME = "Guest";
	
	// Default state
	private boolean loginSuccessful = false;
	
	// Components used by the actionPerformed method
	private JButton submitButton, exitButton;
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	/**
	 * This constructor creates the GUI.
	 */
	public LoginFrame() {
		
		// Submit and exit buttons
		submitButton = new JButton("Submit");
		submitButton.setSize(new Dimension(100,50));
		submitButton.setFocusable(false);
		submitButton.addActionListener(this);
		submitButton.setMnemonic(KeyEvent.VK_ENTER); // Alt + Enter shortcut
		
		exitButton = new JButton("Exit");
		exitButton.setSize(new Dimension(100,50));
		exitButton.setFocusable(false);
		exitButton.addActionListener(this);
		
		// Panel to contain and organize the buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,50));
		buttonsPanel.add(submitButton);
		buttonsPanel.add(exitButton);
		
		// JTextFields for entering username and password
		usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(300,30));
		
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(300,30));
		
		// Labels for username and password boxes
		JLabel usernameLabel = new JLabel();
		usernameLabel.setText("Username: ");
		
		JLabel passwordLabel = new JLabel();
		passwordLabel.setText("Password: ");
		
		// Flow layout panel for username/password input
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		textPanel.add(usernameLabel);
		textPanel.add(usernameField);
		textPanel.add(passwordLabel);
		textPanel.add(passwordField);
		
		// Formatting main JFrame
		this.setTitle("Login");
		this.setLayout(new GridLayout(2,1));
		this.add(textPanel);
		this.add(buttonsPanel);
		this.setSize(400,225);
		this.setResizable(false);
		this.setIconImage(Main.titleFrame.ICON.getImage());
		this.setVisible(true);
		
	}
	
	/**
	 * Takes input from the buttons and either exits or attempts to submit login data.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Closes when exit button is pressed
		if (e.getSource() == exitButton) {
			this.dispose();
		}
		
		if (e.getSource() == submitButton) {
			
			// If the username and password both have input, 
			// stores values and moves on to profile creation
			if (validateUsername(usernameField.getText()) 
					&& validatePassword(new String(passwordField.getPassword()))) {

				checkLoginDetails(usernameField.getText(), new String(passwordField.getPassword()));
				
				if (loginSuccessful) {
					this.dispose();
				}
				
			}
			// If one of the text fields is empty, displays an error
			else {
				JOptionPane.showMessageDialog(null, "Please enter a username between 3 and 16"
						+ " alphanumeric \ncharacters long and a password"
						+ " at least 8 characters long.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	/**
	 * Validates a password using regex according to the stated criteria (at least 8 characters long)
	 * @param password the password to be checked
	 * @return if the password matches regex
	 */
	public static boolean validatePassword(String password) {
		return Pattern.matches("[\\w!@#$%^&*]{8,}", password);
	}
	/**
	 * Validates a password using regex according to the stated criteria (between 3 and 16
	 * alphanumeric characters long)
	 * @param username the username to be checked
	 * @return if the username matches regex
	 */
	public static boolean validateUsername(String username) {
		return Pattern.matches("[\\w]{3,16}", username);
	}
	
	/**
	 * Reads login details from loginDetails.txt, if the input matches, user gets logged in, if
	 * the input does not, asks if the user would like to create a new account and adds new login 
	 * data to loginDetails.txt.
	 * @param username
	 * @param password
	 */

	private void checkLoginDetails(String username, String password) {
		
        boolean loginMatches = false;
        boolean userExists = false;
        
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(FileSetup.getDirectoryPath() + "loginDetails.json");
        LoginDetails loginAttempt = new LoginDetails(username, password);
        
        ArrayList<LoginDetails> knownLogins = new ArrayList<>();
        
        // Reads file to ArrayList
        try {
	        if (file.length() > 0) {
	            knownLogins = objectMapper.readValue(file,
	                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, LoginDetails.class));
	        }
	        else {
	        		throw new EmptyFileException();
	        }
	    } 
        catch (EmptyFileException e) {
			FileSetup.validateFiles();
			e.printStackTrace();
		}
	    catch (IOException e) {
	        e.printStackTrace();
	    }
        
        // Checks if username/password combo works
        for (int i = 0; i < knownLogins.size(); ++i) {
        		// Checks if username and password both match
        		if (loginAttempt.equals(knownLogins.get(i))) {
        			loginMatches = true;
        			Main.titleFrame.setUsername(username);
        			this.dispose();
        			break;
        		}
        		// Checks if just username matches
        		else if (loginAttempt.getUsername().equals(knownLogins.get(i).getUsername())) {
        			userExists = true;
        			break;
        		}
        }
        
        
		try {
			
            /* If the login doesn't match anything in the file, 
            * asks user if they want to create a new account */
			if (!userExists && !loginMatches) {
				if (JOptionPane.showConfirmDialog(null,
						"Login combination not found. Would you " + "like to create a new account?", "Warning",
						JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
					
					// If the user wants to make a new account, adds to file
					knownLogins.add(loginAttempt);
					objectMapper.writeValue(file, knownLogins);
					
					Main.titleFrame.setUsername(username);
					this.dispose();

				} else {
					loginSuccessful = false;
				}
			}
			// Otherwise, asks user to try again
			else if (userExists && !loginMatches) {
				JOptionPane.showConfirmDialog(null, "Password incorrect, please try again.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
        
        catch (FileNotFoundException e) {
        		System.out.println("File not found!");
        		FileSetup.validateFiles();
        }
		
		catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}
	
	public static class LoginDetails {
		
		String username;
		String password;
		
		// Getters, setters, and empty constructor necessary for Jackson
		public LoginDetails() {
			
		}
		
		public LoginDetails(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public String getUsername() {
			return username;
		}
		
		public void setUsername(String username) {
			this.username = username;
		}
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
	}
	
}


