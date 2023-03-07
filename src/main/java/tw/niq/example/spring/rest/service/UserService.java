package tw.niq.example.spring.rest.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import tw.niq.example.spring.rest.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto);

	List<UserDto> getAllUsers(Integer page, Integer limit);

	UserDto getUserByUserId(String userId);
	
	UserDto getUserByEmail(String email);

	UserDto updateUserByUserId(String userId, UserDto userDto);

	void deletetUserByUserId(String userId);

}
