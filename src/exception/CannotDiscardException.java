package exception;

// Class representing an exception that can occur whenever an invalid card discard occurs.

public class CannotDiscardException extends ActionException{

	public CannotDiscardException()
	{
		// Constructs an CannotDiscardException with no detailed message.
		super();
	}

	public CannotDiscardException(String message)
	{
		/**
		 * Constructs an CannotDiscardException with a specified error message.
		 *
		 * @param message The detail message explaining the cause of the exception.
		 */
		super(message);
	}

}
