package br.com.smartconsulting.ordermanager.api.order;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.user.UserEntity;

@Component
public class OrderMapper {
	
	public OrderEntity toEntity(OrderWritingDTO dto) {
		UserEntity user = new UserEntity();
		user.setId(dto.getUserId());
		
		ProductEntity product = new ProductEntity();
		product.setId(dto.getProductId());
		
		OrderEntity entity = new OrderEntity();
		entity.setUser(user);
		entity.setProduct(product);
		entity.setQuantity(dto.getQuantity());
		return entity;
	}

	public OrderEntity toEntity(Long id, OrderWritingDTO dto) {
		OrderEntity entity = this.toEntity(dto);
		entity.setId(id);
		return entity;
	}
	
	public OrderReadingDTO toDTO(OrderEntity entity) {
		LocalDateTime createdAt = entity.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		return new OrderReadingDTO(
			entity.getId(), 
			entity.getProduct().getId(),
			entity.getUser().getId(),
			entity.getQuantity(),
			entity.getStatus(),
			createdAt
		);
	}
	
	public List<OrderReadingDTO> toDTO(List<OrderEntity> entities) {
		return entities.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}
}
