package exception;

/**
 * Represents an exception that occurs when an invalid selection is made.
 * This is an abstract class extending GameException, meaning it cannot be instantiated directly.
 * It serves as the base class for more specific selection-related exceptions.
 */

public abstract class InvalidSelectionException extends GameException{

	public InvalidSelectionException()
	{
		// Constructs an InvalidSelectionException with no detailed message.
		super();
	}


	public InvalidSelectionException(String message)
	{
		/**
		 * Constructs an InvalidSelectionException with a specified error message.
		 *
		 * @param message The detail message explaining the cause of the exception.
		 */
		super(message);
	}
}
