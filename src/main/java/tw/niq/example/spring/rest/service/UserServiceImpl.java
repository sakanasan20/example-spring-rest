package tw.niq.example.spring.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.entity.UserEntity;
import tw.niq.example.spring.rest.exception.BadRequestUserException;
import tw.niq.example.spring.rest.exception.ResourceNotFoundUserException;
import tw.niq.example.spring.rest.exception.UserException;
import tw.niq.example.spring.rest.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntityFound = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(email));
		
		return new User(userEntityFound.getEmail(), userEntityFound.getPassword(), new ArrayList<>());
	}
	
	@Transactional
	@Override
	public UserDto createUser(UserDto userDtoToCreate) {
		
		if (userRepository.findByEmail(userDtoToCreate.getEmail()).isPresent()) {
			throw new BadRequestUserException("Duplicated email: " + userDtoToCreate.getEmail());
		}
		
		userDtoToCreate.setUserId(UUID.randomUUID().toString());
		userDtoToCreate.setPassword(bCryptPasswordEncoder.encode(userDtoToCreate.getPassword()));
		UserEntity userEntityToSave = modelMapper.map(userDtoToCreate, UserEntity.class);
		UserEntity userEntitySaved = userRepository.save(userEntityToSave);
		UserDto userDtoCreated = modelMapper.map(userEntitySaved, UserDto.class);
		
		return userDtoCreated;
	}

	@Override
	public List<UserDto> getAllUsers(Integer page, Integer limit) {
		
		if (page > 0) page--;
		PageRequest pageRequest = PageRequest.of(page, limit);
		Page<UserEntity> userEntityPageFound = userRepository.findAll(pageRequest);
		List<UserEntity> userEntityListFound = userEntityPageFound.getContent();
		List<UserDto> userDtoListGot = userEntityListFound.stream()
				.map(userEntityFound -> modelMapper.map(userEntityFound, UserDto.class))
				.collect(Collectors.toList());
		
		return userDtoListGot;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userEntityFound = userRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundUserException("User not found: " + userId));
		UserDto userDtoGot = modelMapper.map(userEntityFound, UserDto.class);
		
		return userDtoGot;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity userEntityFound = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserException("User not found: " + email));
		UserDto userDtoGot = modelMapper.map(userEntityFound, UserDto.class);
		
		return userDtoGot;
	}

	@Transactional
	@Override
	public UserDto updateUserByUserId(String userId, UserDto userDto) {
		
		UserEntity userEntityToUpdate = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UserException("User not found: " + userId));
		userEntityToUpdate.setEmail(userDto.getEmail());
		UserEntity userEntityUpdated = userRepository.save(userEntityToUpdate);
		UserDto userDtoUpdated = modelMapper.map(userEntityUpdated, UserDto.class);
		
		return userDtoUpdated;
	}

	@Transactional
	@Override
	public void deletetUserByUserId(String userId) {

		UserEntity userEntityToDelete = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UserException("User not found: " + userId));
		userRepository.delete(userEntityToDelete);
	}

}
