package tw.niq.example.spring.rest.service;

import tw.niq.example.spring.rest.dto.RoleDto;

public interface RoleService {

	RoleDto getRoleByName(String name);
	
}
