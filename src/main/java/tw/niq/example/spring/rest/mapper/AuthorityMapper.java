package tw.niq.example.spring.rest.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import tw.niq.example.spring.rest.dto.AuthorityDto;
import tw.niq.example.spring.rest.entity.AuthorityEntity;
import tw.niq.example.spring.rest.model.response.AuthorityModel;

@Component
public class AuthorityMapper extends ModelMapper {

	public AuthorityMapper() {
		this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public AuthorityDto mapToDto(AuthorityModel model) {
		return map(model, AuthorityDto.class);
	}
	
	public AuthorityEntity mapToEntity(AuthorityDto dto) {
		return map(dto, AuthorityEntity.class);
	}
	
	public AuthorityDto mapToDto(AuthorityEntity entity) {
		return map(entity, AuthorityDto.class);
	}
	
	public AuthorityModel mapToModel(AuthorityDto dto) {
		return map(dto, AuthorityModel.class);
	}
	
}
