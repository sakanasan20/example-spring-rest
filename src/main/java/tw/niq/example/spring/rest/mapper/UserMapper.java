package tw.niq.example.spring.rest.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.entity.UserEntity;
import tw.niq.example.spring.rest.model.request.CreateUserRequestModel;
import tw.niq.example.spring.rest.model.request.UpdateUserRequestModel;
import tw.niq.example.spring.rest.model.response.UserResponseModel;

@Component
public class UserMapper extends ModelMapper {

	public UserMapper() {
		this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public UserDto mapToDto(CreateUserRequestModel model) {
		return map(model, UserDto.class);
	}
	
	public UserDto mapToDto(UpdateUserRequestModel model) {
		return map(model, UserDto.class);
	}
	
	public UserEntity mapToEntity(UserDto dto) {
		return map(dto, UserEntity.class);
	}
	
	public UserDto mapToDto(UserEntity entity) {
		return map(entity, UserDto.class);
	}
	
	public UserResponseModel mapToModel(UserDto dto) {
		return map(dto, UserResponseModel.class);
	}
	
}
