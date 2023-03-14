package tw.niq.example.spring.rest.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.niq.example.spring.rest.dto.RoleDto;
import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.entity.AuthorityEntity;
import tw.niq.example.spring.rest.entity.RoleEntity;
import tw.niq.example.spring.rest.entity.UserEntity;
import tw.niq.example.spring.rest.exception.BadRequestException;
import tw.niq.example.spring.rest.exception.ResourceNotFoundException;
import tw.niq.example.spring.rest.mapper.UserMapper;
import tw.niq.example.spring.rest.repository.RoleRepository;
import tw.niq.example.spring.rest.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

		return new User(
				user.getEmail(), 
				user.getPassword(), 
				user.getEnabled(), 
				user.getAccountNonExpired(), 
				user.getCredentialsNonExpired(), 
				user.getAccountNonLocked(), 
				convertRolesAndAuthorities(user)
		);
	}

	private Set<SimpleGrantedAuthority> convertRolesAndAuthorities(UserEntity user) {
		
		Set<SimpleGrantedAuthority> roles = user.getRoles().stream()
				.map(RoleEntity::getRoleName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
		Set<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
				.map(AuthorityEntity::getAuthorityName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
		Set<SimpleGrantedAuthority> rolesAndAuthorities = new HashSet<>();
		
		rolesAndAuthorities.addAll(roles);
		rolesAndAuthorities.addAll(authorities);
		
		return rolesAndAuthorities;
	}
	
	@Transactional
	@Override
	public UserDto createUser(UserDto user) {
		
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new BadRequestException(user.getEmail());
		}

		UserEntity userToSave = userMapper.mapToEntity(user);
		
		userToSave.setPassword(bCryptPasswordEncoder.encode(userToSave.getPassword()));
		
		Set<RoleEntity> rolesToSave = user.getRoles().stream()
				.map(RoleDto::getRoleName)
				.map( roleName -> {
					RoleEntity role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException(roleName));
					return role;
				})
				.collect(Collectors.toSet());
		
		userToSave.setRoles(rolesToSave);
		
		UserEntity userSaved = userRepository.save(userToSave);
		UserDto returnValue = userMapper.mapToDto(userSaved);
		
		return returnValue;
	}

	@Override
	public List<UserDto> getAllUsers(Integer page, Integer limit) {
		
		if (page > 0) page--;
		PageRequest pageRequest = PageRequest.of(page, limit);
		Page<UserEntity> userPage = userRepository.findAll(pageRequest);
		List<UserEntity> userList = userPage.getContent();
		List<UserDto> returnValue = userList.stream().map(userMapper::mapToDto).collect(Collectors.toList());
		
		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
		UserDto returnValue = userMapper.mapToDto(user);
		
		return returnValue;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(email));
		UserDto returnValue = userMapper.mapToDto(user);
		
		return returnValue;
	}

	@Transactional
	@Override
	public UserDto updateUserByUserId(String userId, UserDto user) {
		
		UserEntity userToUpdate = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
		
		if (user.getUsername() != null && (userToUpdate.getUsername() == null || !userToUpdate.getUsername().equals(user.getUsername()))) {
			userToUpdate.setUsername(user.getUsername());
		}
		
		if (user.getEmail() != null && !userToUpdate.getEmail().equals(user.getEmail())) {
			userToUpdate.setEmail(user.getEmail());
		}
		
		UserEntity userUpdated = userRepository.save(userToUpdate);
		UserDto returnValue = userMapper.mapToDto(userUpdated);
		
		return returnValue;
	}

	@Transactional
	@Override
	public void deletetUserByUserId(String userId) {

		UserEntity userEntityToDelete = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
		userRepository.delete(userEntityToDelete);
	}

}
