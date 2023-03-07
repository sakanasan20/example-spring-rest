package tw.niq.example.spring.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import tw.niq.example.spring.rest.exception.BadRequestUserException;
import tw.niq.example.spring.rest.model.request.CreateUserRequestModel;
import tw.niq.example.spring.rest.model.request.UpdateUserRequestModel;
import tw.niq.example.spring.rest.model.response.UserResponseModel;
import tw.niq.example.spring.rest.service.UserService;

@RestController
@RequestMapping(path = UserController.PATH)
public class UserController {

	public static final String PATH = "/api/v1/users";
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	private ModelMapper modelMapper;

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
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseModel createUser(@Valid @RequestBody CreateUserRequestModel createUserRequestModel, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = bindingResult.getAllErrors().stream()
					.map(ObjectError::getDefaultMessage)
					.collect(Collectors.toList());
			throw new BadRequestUserException("Fields error: " + errorMessages);
		}
			
		UserDto userDtoToCreate = modelMapper.map(createUserRequestModel, UserDto.class);
		UserDto userDtoCreated = userService.createUser(userDtoToCreate);
		UserResponseModel userResponseModel = modelMapper.map(userDtoCreated, UserResponseModel.class);
		
		return userResponseModel;
	}
	
	/**
	 * Get all users
	 * @return
	 */
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserResponseModel> getUsers(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "limit", defaultValue = "5") Integer limit) {
		
		List<UserDto> userDtoListGot = userService.getAllUsers(page, limit);
		List<UserResponseModel> userResponseModelList = userDtoListGot.stream()
				.map(userDtoGot -> modelMapper.map(userDtoGot, UserResponseModel.class))
				.collect(Collectors.toList());
		
		return userResponseModelList;
	}
	
	/**
	 * Get user
	 * @return
	 */
	@GetMapping(
			path = "/{userId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseModel getUser(@PathVariable("userId") String userId) {
		
		UserDto userDtoGot = userService.getUserByUserId(userId);
		UserResponseModel userResponseModel = modelMapper.map(userDtoGot, UserResponseModel.class);
		
		return userResponseModel;
	}
	
	/**
	 * Update user
	 * @return
	 */
	@PutMapping(
			path = "/{userId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponseModel updateUser(@PathVariable("userId") String userId, 
			@Valid @RequestBody UpdateUserRequestModel updateUserRequestModel, BindingResult bindingResult) {
		
		UserDto userDtoToUpdate = modelMapper.map(updateUserRequestModel, UserDto.class);
		UserDto userDtoUpdated = userService.updateUserByUserId(userId, userDtoToUpdate);
		UserResponseModel userResponseModel = modelMapper.map(userDtoUpdated, UserResponseModel.class);
		
		return userResponseModel;
	}
	
	/**
	 * Delete user
	 * @return
	 */
	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
		
		userService.deletetUserByUserId(userId);
		
		return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);
	}
	
}
