package exception;

/**
 * Class representing an exception that can occur whenever an illegal move is
 * done throughout the game. This class is a subclass of GameException class.
 * This class cannot be instantiated
 */

public abstract class ActionException extends GameException {

	/**
	 * Constructs an ActionException with no detailed message
	 */

	public ActionException() {
		super();
	}

	/**
	 * Constructs an ActionException with a specified error message.
	 *
	 * @param message The detail message explaining the cause of the exception.
	 */
	public ActionException(String message) {
		super(message);
	}

}
