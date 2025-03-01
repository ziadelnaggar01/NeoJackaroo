package exception;

/**
 * A subclass from ActionException class representing an exception that can occur whenever an invalid card
 * discard occurs.
 */

public class CannotDiscardException extends ActionException {

	/**
	 * Constructs an CannotDiscardException with no detailed message.
	 */

	public CannotDiscardException() {
		super();
	}

	/**
	 * Constructs an CannotDiscardException with a specified error message.
	 *
	 * @param message The detail message explaining the cause of the exception.
	 */
	public CannotDiscardException(String message) {

		super(message);
	}

}
