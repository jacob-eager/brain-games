package dev.jacobeager;

/**
 * The driver class for the program.
 * @author Jacob Eager
 * @version 1.0
 */

public class Main {
	
	// Declared as a static field for usage in other classes
	public static TitleFrame titleFrame;
	
	public static void main(String[] args) {
		
		// Begins setup of required external files
		FileSetup.startSetup();
		
		// Starts program with the title screen
		titleFrame = new TitleFrame();
		
	}
}
