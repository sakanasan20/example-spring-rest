package tw.niq.example.spring.rest.dto;

import java.io.Serializable;
import java.util.Set;

public class RoleDto implements Serializable {

	private static final long serialVersionUID = -4727671875361685072L;

	private Long id;
	
	private String roleId;
	
	private String roleName;
	
	private Set<AuthorityDto> authorities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<AuthorityDto> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityDto> authorities) {
		this.authorities = authorities;
	}

}
