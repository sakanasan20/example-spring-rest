package tw.niq.example.spring.rest.model.request;

import jakarta.validation.constraints.Email;

public class UpdateUserRequestModel {

	@Email(message = "Email is not valid")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
