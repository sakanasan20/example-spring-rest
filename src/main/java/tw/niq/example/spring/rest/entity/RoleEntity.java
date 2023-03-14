package tw.niq.example.spring.rest.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "rest_role")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String roleId = UUID.randomUUID().toString();
	
	@Column(nullable = false, unique = true)
	private String roleName;
	
	@ManyToMany(mappedBy = "roles")
	private Set<UserEntity> users;
	
	@ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(
			name = "role_authority", 
			joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }, 
			inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
	private Set<AuthorityEntity> authorities;

	public RoleEntity() {
	}
	
	public RoleEntity(String roleName) {
		this.roleName = roleName;
	}

	public RoleEntity(String roleName, Set<AuthorityEntity> authorities) {
		this.roleName = roleName;
		this.authorities = authorities;
	}

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

	public Set<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserEntity> users) {
		this.users = users;
	}

	public Set<AuthorityEntity> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<AuthorityEntity> authorities) {
		this.authorities = authorities;
	}
	
}
