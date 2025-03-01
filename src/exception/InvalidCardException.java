package exception;

/**
 * Class representing an exception that can occur whenever an invalid card is
 * selected in the game.
 */
public class InvalidCardException extends InvalidSelectionException {
	/** Constructs an InvalidCardException with no detailed message. */

	public InvalidCardException() {
		super();
	}

	/**
	 * Constructs an InvalidCardException with a specified error message.
	 *
	 * @param message The detail message explaining the cause of the exception.
	 */

	public InvalidCardException(String message) {
		super(message);
	}
}
