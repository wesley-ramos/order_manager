package br.com.smartconsulting.ordermanager.api.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.smartconsulting.ordermanager.core.user.UserEntity;

@Component
public class UserMapper {
	
	public UserEntity toEntity(UserWritingDTO dto) {
		UserEntity entity = new UserEntity();
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		return entity;
	}

	public UserEntity toEntity(Long id, UserWritingDTO dto) {
		UserEntity entity = this.toEntity(dto);
		entity.setId(id);
		return entity;
	}
	
	public UserReadingDTO toDTO(UserEntity entity) {
		return new UserReadingDTO(
			entity.getId(), 
			entity.getName(), 
			entity.getEmail()
		);
	}
	
	public List<UserReadingDTO> toDTO(List<UserEntity> entities) {
		return entities.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}
}
