package br.com.smartconsulting.ordermanager.api.stock;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementEntity;

@Component
public class StockMovementMapper {
	
	public StockMovementEntity toEntity(StockMovementWritingDTO dto) {
		ProductEntity product = new ProductEntity();
		product.setId(dto.getProductId());
		
		StockMovementEntity entity = new StockMovementEntity();
		entity.setProduct(product);
		entity.setQuantity(dto.getQuantity());
		return entity;
	}

	public StockMovementEntity toEntity(Long id, StockMovementWritingDTO dto) {
		StockMovementEntity entity = this.toEntity(dto);
		entity.setId(id);
		return entity;
	}
	
	public StockMovementReadingDTO toDTO(StockMovementEntity entity) {
		LocalDateTime createdAt = entity.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		return new StockMovementReadingDTO(
			entity.getId(),
			entity.getProduct().getId(),
			entity.getQuantity(),
			createdAt
		);
	}
	
	public List<StockMovementReadingDTO> toDTO(List<StockMovementEntity> entities) {
		return entities.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}
}
