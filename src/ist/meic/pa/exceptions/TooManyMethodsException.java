package ist.meic.pa.exceptions;

public class TooManyMethodsException extends Exception {

	private static final long serialVersionUID = 1L;

	public TooManyMethodsException() {
		super();
	}

	public TooManyMethodsException(String message, Throwable cause) {
		super(message, cause);
	}

	public TooManyMethodsException(String message) {
		super(message);
	}

	public TooManyMethodsException(Throwable cause) {
		super(cause);
	}

}
