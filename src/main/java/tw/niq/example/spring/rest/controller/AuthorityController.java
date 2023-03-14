package tw.niq.example.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.niq.example.spring.rest.dto.AuthorityDto;
import tw.niq.example.spring.rest.mapper.AuthorityMapper;
import tw.niq.example.spring.rest.model.response.AuthorityModel;
import tw.niq.example.spring.rest.service.AuthorityService;

@RestController
@RequestMapping(path = AuthorityController.PATH)
public class AuthorityController {
	
	public static final String PATH = "/api/v1/authorities";
	
	private final AuthorityService authorityService;

	public AuthorityController(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}
	
	@Autowired
	private AuthorityMapper authorityMapper;

	@GetMapping(
			path = "/{authorityId}", 
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public EntityModel<AuthorityModel> getAuthority(@PathVariable("authorityId") String authorityId) {
		
		AuthorityDto authority = authorityService.getAuthorityByAuthorityId(authorityId);
		AuthorityModel returnValue = authorityMapper.mapToModel(authority);
		
		Link selfLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(AuthorityController.class).getAuthority(authorityId))
				.withSelfRel();
		
		return EntityModel.of(returnValue, selfLink);
	}
	
}
