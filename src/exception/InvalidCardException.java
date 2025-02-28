package exception;
//Class representing an exception that can occur whenever an invalid card is selected in the game.
public class InvalidCardException extends InvalidSelectionException {

	public InvalidCardException() {
		super();
	}
	public InvalidCardException(String message) {
		super(message);
	}
}
