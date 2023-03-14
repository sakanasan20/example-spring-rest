package tw.niq.example.spring.rest.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.entity.UserEntity;
import tw.niq.example.spring.rest.model.CreateUserModel;
import tw.niq.example.spring.rest.model.UpdateUserModel;
import tw.niq.example.spring.rest.model.UserModel;

@Component
public class UserMapper extends ModelMapper {

	public UserMapper() {
		this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public UserDto mapToDto(CreateUserModel model) {
		return map(model, UserDto.class);
	}
	
	public UserDto mapToDto(UpdateUserModel model) {
		return map(model, UserDto.class);
	}
	
	public UserEntity mapToEntity(UserDto dto) {
		return map(dto, UserEntity.class);
	}
	
	public UserDto mapToDto(UserEntity entity) {
		return map(entity, UserDto.class);
	}
	
	public UserModel mapToModel(UserDto dto) {
		return map(dto, UserModel.class);
	}
	
}
