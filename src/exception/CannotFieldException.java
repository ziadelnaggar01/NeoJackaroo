package exception;

/**
 * A subclass from ActionException representing an exception that can occur
 * whenever a marble cannot be placed on board
 */
public class CannotFieldException extends ActionException {

	/**
	 * Constructs an CannotFieldException with no detailed message.
	 */

	public CannotFieldException() {
		super();
	}

	/**
	 * Constructs an CannotFieldException with a specified error message.
	 *
	 * @param message The detail message explaining the cause of the exception.
	 */
	public CannotFieldException(String message) {
		super(message);
	}

}
