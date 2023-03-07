package tw.niq.example.spring.rest.dto;

import java.io.Serializable;

public class UserDto implements Serializable {

	private static final long serialVersionUID = 4505200518062476939L;

	private Long id;
	
	private String userId;

	private String email;
	
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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
		return "UserDto [id=" + id + ", userId=" + userId + ", email=" + email + ", password=" + password + "]";
	}
	
}
