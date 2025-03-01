package exception;

/**
 * Class representing an exception that can occur whenever an invalid action
 * happens that would lead to returning the marble to its Home Zone. This class
 * is a subclass of ActionException class
 */
public class IllegalDestroyException extends ActionException {

	/**
	 * Constructs an IllegalDestroyException with no detailed message.
	 */
	public IllegalDestroyException() {
		super();
	}

	/**
	 * Constructs an IllegalDestroyException with a specified error message.
	 *
	 * @param message The detail message explaining the cause of the exception.
	 */

	public IllegalDestroyException(String message) {
		super(message);
	}

}
