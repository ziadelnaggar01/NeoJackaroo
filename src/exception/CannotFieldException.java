package exception;
// : Class representing an exception that can occur whenever a marble cannot be placed on board
public class CannotFieldException extends ActionException  {
	public CannotFieldException() {
		super();
	}
	public CannotFieldException(String message) {
		super(message);
	}

}
