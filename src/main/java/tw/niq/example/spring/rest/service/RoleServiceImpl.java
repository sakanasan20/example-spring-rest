package tw.niq.example.spring.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.niq.example.spring.rest.dto.RoleDto;
import tw.niq.example.spring.rest.entity.RoleEntity;
import tw.niq.example.spring.rest.exception.ResourceNotFoundException;
import tw.niq.example.spring.rest.mapper.RoleMapper;
import tw.niq.example.spring.rest.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public RoleDto getRoleByName(String name) {
		
		RoleEntity role = roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(name));
		RoleDto returnValue = roleMapper.mapToDto(role);
		
		return returnValue;
	}
	
}
