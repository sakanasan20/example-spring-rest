package tw.niq.example.spring.rest.model.response;

import java.util.Set;

public class UserResponseModel {

	private String userId;
	
	private String username;
	
	private String email;
	
	private Set<RoleModel> roles;

	private Set<AuthorityModel> authorities;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public Set<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleModel> roles) {
		this.roles = roles;
	}

	public Set<AuthorityModel> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityModel> authorities) {
		this.authorities = authorities;
	}
	
}
