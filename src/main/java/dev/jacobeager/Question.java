package dev.jacobeager;

/**
 * Functional Interface for the two types of quiz questions.
 * @author Jacob Eager
 * @version 1.0
 */

@FunctionalInterface
public interface Question {

	public void displayQuestion(QuizBowl frame);

}
