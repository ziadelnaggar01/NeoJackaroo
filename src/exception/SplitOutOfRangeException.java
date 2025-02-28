package exception;

// Class representing an exception that can occur whenever an invalid split distance is chosen.

public class SplitOutOfRangeException extends InvalidSelectionException{

	public SplitOutOfRangeException()
	{
		// Constructs an SplitOutOfRangeException with no detailed message.
		super();
	}

	public SplitOutOfRangeException(String message)
	{
		/**
		 * Constructs an SplitOutOfRangeException with a specified error message.
		 *
		 * @param message The detail message explaining the cause of the exception.
		 */
		super(message);
	}

}
