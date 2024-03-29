package tw.niq.example.spring.rest.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateUserModel {
	
	private String userId = UUID.randomUUID().toString();
	
	private String username;

	@Size(min = 8, max = 16, message = "Password must be between 8-16 characters")
	@NotEmpty(message = "Password must not be empty")
	private String password;
	
	@Email(message = "Email is not valid")
	private String email;
	
	private Boolean accountNonExpired = true;

	private Boolean accountNonLocked = true;

	private Boolean credentialsNonExpired = true;

	private Boolean enabled = true;
	
	private Set<RoleModel> roles = new HashSet<>();
	
	private Set<AuthorityModel> authorities = new HashSet<>();

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
