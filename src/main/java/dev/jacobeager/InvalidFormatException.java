package dev.jacobeager;

/**
 * This exception is called upon when one of files being read by the program are found 
 * to be incorrectly formatted. Note that not all ways a file can be incorrectly formatted are caught.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class InvalidFormatException extends Exception {

	public InvalidFormatException() {
		super("Invalid Format!");
	}
	
}
