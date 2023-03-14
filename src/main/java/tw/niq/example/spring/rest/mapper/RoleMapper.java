package tw.niq.example.spring.rest.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import tw.niq.example.spring.rest.dto.RoleDto;
import tw.niq.example.spring.rest.entity.RoleEntity;
import tw.niq.example.spring.rest.model.RoleModel;

@Component
public class RoleMapper extends ModelMapper {

	public RoleMapper() {
		this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public RoleDto mapToDto(RoleModel model) {
		return map(model, RoleDto.class);
	}
	
	public RoleEntity mapToEntity(RoleDto dto) {
		return map(dto, RoleEntity.class);
	}
	
	public RoleDto mapToDto(RoleEntity entity) {
		return map(entity, RoleDto.class);
	}
	
	public RoleModel mapToModel(RoleDto dto) {
		return map(dto, RoleModel.class);
	}
	
}
