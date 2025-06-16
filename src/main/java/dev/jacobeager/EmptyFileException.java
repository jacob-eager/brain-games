package dev.jacobeager;

/**
 * This exception is called upon when one of the many files read by this program are empty.
 * 
 * @author Jacob Eager
 * @version 1.0
 */

@SuppressWarnings("serial")
public class EmptyFileException extends Exception {

	public EmptyFileException() {
		super("File is empty");
	}
}
