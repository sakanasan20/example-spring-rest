package tw.niq.example.spring.rest.dto;

import java.io.Serializable;

public class AuthorityDto implements Serializable {

	private static final long serialVersionUID = -6374090221324937410L;

	private Long id;
	
	private String name;

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

	@Override
	public String toString() {
		return "AuthorityDto [id=" + id + ", name=" + name + "]";
	}

}
