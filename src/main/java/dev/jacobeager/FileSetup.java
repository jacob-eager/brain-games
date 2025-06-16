package dev.jacobeager;

import java.io.*;
import java.util.Scanner;


public class FileSetup {
	
	// Enumeration for the different operating systems supported
	private static enum OS {
		WINDOWS, MAC, LINUX, OTHER
	}

	// Keeps track of the OS being used
	private static OS computerOS;
	
	
	// Begins the file setup process
	public static void startSetup() {

		detectOS();
		validateFiles();

	}
	
	// Checks if the necessary files exist in the correct directory
	public static void validateFiles() {
		
		
		String[] fileNames = {"hangmanLeaderboard.json", "hangman.txt", "loginDetails.json", 
				"multipleChoiceQuestions.json", "quizLeaderboard.json", "textQuestions.json", 
				"wordle.txt", "wordleLeaderboard.json"};
				
		String dirPath = getDirectoryPath();
		
		File directory = new File(dirPath);
		
		File tempFile;
		
		try {

			// Creates directory if it doesn't exist
			if (!directory.exists()) {
				if (!directory.mkdirs()) {
					throw new SetupFailedException();
				}
			}

			// Checks if each file exists and has text, writes to it if not
			for (String fileName : fileNames) {

				tempFile = new File(dirPath + fileName);

				if (!tempFile.exists()) {
					if (!tempFile.createNewFile()) {
						throw new SetupFailedException();
					}
				} 
				if (tempFile.length() == 0) {
					writeToFile(dirPath + fileName, fileName);
				}

			}
		} catch (SetupFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	// Writes to empty files using the default values stored in the resources folder
	private static void writeToFile(String filePath, String fileName) {
		
		try {
			InputStream inStream = FileSetup.class.getResourceAsStream("/" + fileName);
			Scanner inFS = new Scanner(inStream);
			FileWriter outputStream = new FileWriter(filePath, true);
			BufferedWriter outFS = new BufferedWriter(outputStream);
			String fileContents = "";
			
			
			while (inFS.hasNext()) {
				fileContents += inFS.nextLine() + "\n";
			}
			
			inFS.close();
			inStream.close();
			
			outFS.write(fileContents);
			
			outFS.close();
			outputStream.close();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// Checks operating system
	private static void detectOS() {
		
		String osName = System.getProperty("os.name").toLowerCase();
		
		if (osName.contains("windows")) {
			computerOS = OS.WINDOWS;
		} 
		else if (osName.contains("mac")) {
			computerOS = OS.MAC;
		}
		else if (osName.contains("linux")) {
			computerOS = OS.LINUX;
		}
		else {
			computerOS = OS.OTHER;
		}
	}
	
	// Returns the path of the directory where text files will be stored (varies depending on operating system)
	public static String getDirectoryPath() {
		
		switch (computerOS) {

		case WINDOWS:
			return "C:\\Brain Games\\";

		case MAC:
			return "/Applications/Brain Games/";

		case LINUX:
			return "/usr/local/bin/BrainGames/";

		case OTHER:
			return "/opt/BrainGames/";

		default:
			break;
			
		}
		return "";
	}

}
