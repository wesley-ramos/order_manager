package br.com.smartconsulting.ordermanager.api.product;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;

@Component
public class ProductMapper {
	
	public ProductEntity toEntity(ProductWritingDTO dto) {
		ProductEntity entity = new ProductEntity();
		entity.setName(dto.getName());
		return entity;
	}

	public ProductEntity toEntity(Long id, ProductWritingDTO dto) {
		ProductEntity entity = this.toEntity(dto);
		entity.setId(id);
		return entity;
	}
	
	public ProductReadingDTO toDTO(ProductEntity entity) {
		return new ProductReadingDTO(
			entity.getId(), 
			entity.getName()
		);
	}
	
	public List<ProductReadingDTO> toDTO(List<ProductEntity> entities) {
		return entities.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}
}
