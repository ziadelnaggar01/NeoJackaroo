package exception;

public class InvalidMarbleException extends InvalidSelectionException {
/*
 Class representing an exception that can occur whenever an invalid marble is selected
 * */
	public InvalidMarbleException()
	{
		super();
	}
	public InvalidMarbleException(String message)
	{
		super(message);
	}
}
