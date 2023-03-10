package tw.niq.example.spring.rest.service;

import java.util.List;
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

import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.entity.AuthorityEntity;
import tw.niq.example.spring.rest.entity.UserEntity;
import tw.niq.example.spring.rest.exception.BadRequestUserException;
import tw.niq.example.spring.rest.exception.ResourceNotFoundUserException;
import tw.niq.example.spring.rest.mapper.UserMapper;
import tw.niq.example.spring.rest.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
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
				user.getAuthorities().stream()
						.map(AuthorityEntity::getName)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet())
		);
	}
	
	@Transactional
	@Override
	public UserDto createUser(UserDto user) {
		
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new BadRequestUserException(user.getEmail());
		}
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserEntity userToSave = userMapper.mapToEntity(user);
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
		
		UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundUserException(userId));
		UserDto returnValue = userMapper.mapToDto(user);
		
		return returnValue;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundUserException(email));
		UserDto returnValue = userMapper.mapToDto(user);
		
		return returnValue;
	}

	@Transactional
	@Override
	public UserDto updateUserByUserId(String userId, UserDto user) {
		
		UserEntity userToUpdate = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundUserException(userId));
		
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

		UserEntity userEntityToDelete = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundUserException(userId));
		userRepository.delete(userEntityToDelete);
	}

}
