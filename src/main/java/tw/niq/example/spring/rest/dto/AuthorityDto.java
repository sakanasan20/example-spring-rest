package tw.niq.example.spring.rest.dto;

import java.io.Serializable;

public class AuthorityDto implements Serializable {

	private static final long serialVersionUID = -6374090221324937410L;

	private Long id;
	
	private String authorityId;
	
	private String authorityName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

}
