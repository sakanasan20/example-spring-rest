package tw.niq.example.spring.rest.model.response;

import java.time.LocalDateTime;

public class ErrorModel {

	private LocalDateTime timestamp;
	
	private String message;

	public ErrorModel(LocalDateTime timestamp, String message) {
		this.timestamp = timestamp;
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ErrorResponseModel [timestamp=" + timestamp + ", message=" + message + "]";
	}

}
