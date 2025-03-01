package exception;

/**
 * Class representing a generic exception that can occur during game play. These
 * exceptions arise from any invalid action that is performed. This class is a
 * subclass of java’s Exception class. No objects of type GameException can be
 * instantiated.
 */

public abstract class GameException extends Exception {

	/**
	 * Constructs an GameException with no detailed message.
	 */
	public GameException() {
		super();
	}

	/**
	 * Constructs an GameException with a specified error message.
	 *
	 * @param message The detail message explaining the cause of the exception.
	 */
	public GameException(String message) {
		super(message);
	}

}
