package tw.niq.example.spring.rest.exception;

public class ResourceNotFoundUserException extends RuntimeException {

	private static final long serialVersionUID = -2015941267648037015L;

	public ResourceNotFoundUserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
