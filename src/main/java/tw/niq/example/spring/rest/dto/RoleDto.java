package tw.niq.example.spring.rest.dto;

import java.io.Serializable;
import java.util.Set;

public class RoleDto implements Serializable {

	private static final long serialVersionUID = -4727671875361685072L;

	private Long id;
	
	private String name;
	
	private Set<AuthorityDto> authorities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<AuthorityDto> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityDto> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "RoleDto [id=" + id + ", name=" + name + "]";
	}

}
