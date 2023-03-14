package tw.niq.example.spring.rest.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import tw.niq.example.spring.rest.exception.BadRequestException;
import tw.niq.example.spring.rest.exception.ResourceNotFoundException;
import tw.niq.example.spring.rest.mapper.UserMapper;
import tw.niq.example.spring.rest.model.request.CreateUserModel;
import tw.niq.example.spring.rest.model.request.UpdateUserModel;
import tw.niq.example.spring.rest.model.response.AuthorityModel;
import tw.niq.example.spring.rest.model.response.RoleModel;
import tw.niq.example.spring.rest.model.response.UserModel;
import tw.niq.example.spring.rest.security.perms.Admin;
import tw.niq.example.spring.rest.security.perms.SelfOnly;
import tw.niq.example.spring.rest.security.perms.UserDelete;
import tw.niq.example.spring.rest.security.perms.UserRead;
import tw.niq.example.spring.rest.security.perms.UserWrite;
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
	@Admin
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel create(@Valid @RequestBody CreateUserModel user, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = bindingResult.getAllErrors().stream()
					.map(ObjectError::getDefaultMessage)
					.collect(Collectors.toList());
			throw new BadRequestException("Fields error: " + errorMessages);
		}
		
		UserDto userToCreate = userMapper.mapToDto(user);
		userToCreate.setRoles(Set.of(roleService.getRoleByRoleName("USER")));
		UserDto userCreated = userService.createUser(userToCreate);
		UserModel returnValue = userMapper.mapToModel(userCreated);
		
		return returnValue;
	}
	
	/**
	 * Get all users
	 * @return
	 */
	@Admin
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public CollectionModel<EntityModel<UserModel>> getUsers(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "limit", defaultValue = "5") Integer limit) {
		
		List<UserDto> userList = userService.getAllUsers(page, limit);
		List<EntityModel<UserModel>> returnValue = userList.stream()
				.map(userMapper::mapToModel)
				.map(user -> {
					
					Link selfLink = WebMvcLinkBuilder
							.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(user.getUserId()))
							.withSelfRel();
					Link rolesLink = WebMvcLinkBuilder
							.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserRoles(user.getUserId()))
							.withRel("roles");
					Link authoritiesLink = WebMvcLinkBuilder
							.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAuthorities(user.getUserId()))
							.withRel("authorities");
					
					return EntityModel.of(user, rolesLink, authoritiesLink, selfLink);
				})
				.collect(Collectors.toList());
				
		Link selfLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(page, limit))
				.withSelfRel();
		
		return CollectionModel.of(returnValue, selfLink);
	}
	
	/**
	 * Get user
	 * @return
	 */
	@UserRead
	@SelfOnly
	@GetMapping(
			path = "/{userId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public EntityModel<UserModel> getUser(@PathVariable("userId") String userId) {
		
		UserDto userDto = userService.getUserByUserId(userId);
		UserModel userModel = userMapper.mapToModel(userDto);
		
		Link selfLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId))
				.withSelfRel();
		Link rolesLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserRoles(userId))
				.withRel("roles");
		Link authoritiesLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAuthorities(userId))
				.withRel("authorities");
		
		return EntityModel.of(userModel, rolesLink, authoritiesLink, selfLink);
	}
	
	/**
	 * Get user's roles
	 * @param userId
	 * @return
	 */
	@UserRead
	@SelfOnly
	@GetMapping(
			path = "/{userId}/roles", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public Set<RoleModel> getUserRoles(@PathVariable("userId") String userId) {
		
		UserDto user = userService.getUserByUserId(userId);
		Set<RoleModel> returnValue = userMapper.mapToModel(user).getRoles();
		
		return returnValue;
	}
	
	/**
	 * Get user's authorities
	 * @param userId
	 * @return
	 */
	@UserRead
	@SelfOnly
	@GetMapping(
			path = "/{userId}/authorities", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public Set<AuthorityModel> getUserAuthorities(@PathVariable("userId") String userId) {
		
		UserDto user = userService.getUserByUserId(userId);
		Set<AuthorityModel> returnValue = userMapper.mapToModel(user).getAuthorities();
		
		return returnValue;
	}
	
	/**
	 * Get user's authorities
	 * @param userId
	 * @return
	 */
	@UserRead
	@SelfOnly
	@GetMapping(
			path = "/{userId}/authorities/{authorityId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public EntityModel<AuthorityModel> getUserAuthority(@PathVariable("userId") String userId, 
			@PathVariable("authorityId") String authorityId) {
		
		UserDto user = userService.getUserByUserId(userId);
		AuthorityModel returnValue = userMapper.mapToModel(user).getAuthorities().stream()
				.filter(authority -> authority.getAuthorityId().equals(authorityId))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException(authorityId));
		
		return EntityModel.of(returnValue);
	}
	
	/**
	 * Update user
	 * @return
	 */
	@UserWrite
	@SelfOnly
	@PutMapping(
			path = "/{userId}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserModel updateUser(@PathVariable("userId") String userId, 
			@Valid @RequestBody UpdateUserModel user, BindingResult bindingResult) {
		
		UserDto userToUpdate = userMapper.mapToDto(user);
		UserDto userUpdated = userService.updateUserByUserId(userId, userToUpdate);
		UserModel returnValue = userMapper.mapToModel(userUpdated);
		
		return returnValue;
	}
	
	/**
	 * Delete user
	 * @return
	 */
	@UserDelete
	@SelfOnly
	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
		
		userService.deletetUserByUserId(userId);
		
		return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);
	}
	
}
