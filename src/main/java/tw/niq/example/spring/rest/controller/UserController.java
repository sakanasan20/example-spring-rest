package tw.niq.example.spring.rest.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.exception.BadRequestException;
import tw.niq.example.spring.rest.mapper.UserMapper;
import tw.niq.example.spring.rest.model.request.CreateUserRequestModel;
import tw.niq.example.spring.rest.model.request.UpdateUserRequestModel;
import tw.niq.example.spring.rest.model.response.UserResponseModel;
import tw.niq.example.spring.rest.service.RoleService;
import tw.niq.example.spring.rest.service.UserService;

@RestController
@RequestMapping(path = UserController.PATH)
public class UserController {

	public static final String PATH = "/api/v1/users";
	
	private final UserService userService;
	private final RoleService roleService;
	
	public UserController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@Autowired
	private UserMapper userMapper;

	/**
	 * Check
	 * @return
	 */
	@GetMapping(path = "/check")
	public String check() {
		return PATH + " is working...";
	}
	
	/**
	 * Create user
	 * @return
	 */
	@PostAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseModel create(@Valid @RequestBody CreateUserRequestModel user, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = bindingResult.getAllErrors().stream()
					.map(ObjectError::getDefaultMessage)
					.collect(Collectors.toList());
			throw new BadRequestException("Fields error: " + errorMessages);
		}
		
		UserDto userToCreate = userMapper.mapToDto(user);
		userToCreate.setRoles(Set.of(roleService.getRoleByName("ROLE_USER")));
		UserDto userCreated = userService.createUser(userToCreate);
		UserResponseModel returnValue = userMapper.mapToModel(userCreated);
		
		return returnValue;
	}
	
	/**
	 * Get all users
	 * @return
	 */
	@PostAuthorize("hasRole('ADMIN')")
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserResponseModel> getUsers(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "limit", defaultValue = "5") Integer limit) {
		
		List<UserDto> userList = userService.getAllUsers(page, limit);
		List<UserResponseModel> returnValue = userList.stream()
				.map(userMapper::mapToModel)
				.collect(Collectors.toList());
		
		return returnValue;
	}
	
	/**
	 * Get user
	 * @return
	 */
	@PostAuthorize("hasRole('ADMIN') or hasAuthority('AUTHORITY_READ') and returnObject.userId == principal.userId")
	@GetMapping(
			path = "/{userId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseModel getUser(@PathVariable("userId") String userId) {
		
		UserDto user = userService.getUserByUserId(userId);
		UserResponseModel returnValue = userMapper.mapToModel(user);
		
		return returnValue;
	}
	
	/**
	 * Update user
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN') or hasAuthority('AUTHORITY_WRITE') and #userId == principal.userId")
	@PutMapping(
			path = "/{userId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseModel updateUser(@PathVariable("userId") String userId, 
			@Valid @RequestBody UpdateUserRequestModel user, BindingResult bindingResult) {
		
		UserDto userToUpdate = userMapper.mapToDto(user);
		UserDto userUpdated = userService.updateUserByUserId(userId, userToUpdate);
		UserResponseModel returnValue = userMapper.mapToModel(userUpdated);
		
		return returnValue;
	}
	
	/**
	 * Delete user
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN') or hasAuthority('AUTHORITY_DELETE') or #userId == principal.userId")
	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
		
		userService.deletetUserByUserId(userId);
		
		return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);
	}
	
}
