package ist.meic.pa.exceptions;

public class QuitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuitException() {
		super();
	}

	public QuitException(String message) {
		super(message);
	}

	public QuitException(Throwable cause) {
		super(cause);
	}

	public QuitException(String message, Throwable cause) {
		super(message, cause);
	}

}
