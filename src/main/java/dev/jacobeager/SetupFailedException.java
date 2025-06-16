package dev.jacobeager;

@SuppressWarnings("serial")
public class SetupFailedException extends Exception{

	public SetupFailedException() {
		super("File setup failed! Check your system privileges.");
	}
	
}
