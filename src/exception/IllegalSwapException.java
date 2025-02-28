package exception;

// Class representing an exception that can occur whenever an illegal marble swap occurs in the game.

public class IllegalSwapException extends ActionException{

	public IllegalSwapException()
	{
		// Constructs an IllegalSwapException with no detailed message.
		super();
	}

	public IllegalSwapException(String message)
	{
		/**
		 * Constructs an IllegalSwapException with a specified error message.
		 *
		 * @param message The detail message explaining the cause of the exception.
		 */
		super(message);
	}

}
