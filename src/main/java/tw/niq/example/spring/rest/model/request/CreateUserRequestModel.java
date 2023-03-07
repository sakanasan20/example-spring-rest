package tw.niq.example.spring.rest.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateUserRequestModel {

	@Email(message = "Email is not valid")
	private String email;
	
	@Size(min = 8, max = 16, message = "Password must be between 8-16 characters")
	@NotEmpty(message = "Password must not be empty")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "CreateUserRequestModel [email=" + email + ", password=" + password + "]";
	}
	
}
