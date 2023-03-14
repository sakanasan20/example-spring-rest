package tw.niq.example.spring.rest.service;

import tw.niq.example.spring.rest.dto.AuthorityDto;

public interface AuthorityService {

	AuthorityDto getAuthorityByAuthorityId(String authorityId);

}
