package tw.niq.example.spring.rest.model.request;

import jakarta.validation.constraints.Email;

public class UpdateUserRequestModel {
	
	private String username;

	@Email(message = "Email is not valid")
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
