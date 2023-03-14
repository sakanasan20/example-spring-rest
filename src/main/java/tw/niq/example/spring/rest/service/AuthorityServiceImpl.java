package tw.niq.example.spring.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.niq.example.spring.rest.dto.AuthorityDto;
import tw.niq.example.spring.rest.entity.AuthorityEntity;
import tw.niq.example.spring.rest.exception.ResourceNotFoundException;
import tw.niq.example.spring.rest.mapper.AuthorityMapper;
import tw.niq.example.spring.rest.repository.AuthorityRepository;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	private final AuthorityRepository authorityRepository;

	public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}
	
	@Autowired
	private AuthorityMapper authorityMapper;

	@Override
	public AuthorityDto getAuthorityByAuthorityId(String authorityId) {
		
		AuthorityEntity authority = authorityRepository.findByAuthorityId(authorityId).orElseThrow(() -> new ResourceNotFoundException(authorityId));
		AuthorityDto returnValue = authorityMapper.mapToDto(authority);
		
		return returnValue;
		
	}

}
