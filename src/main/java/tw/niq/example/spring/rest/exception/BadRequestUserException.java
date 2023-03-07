package tw.niq.example.spring.rest.exception;

public class BadRequestUserException extends RuntimeException {

	private static final long serialVersionUID = -6821146880606613241L;

	public BadRequestUserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadRequestUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BadRequestUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BadRequestUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BadRequestUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
