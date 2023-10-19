package br.com.smartconsulting.ordermanager.api.stock;

import java.time.LocalDateTime;

public class StockMovementReadingDTO {

	private Long id;
	private Long productId;
	private Long quantity;
	private LocalDateTime createdAt;
	
	public StockMovementReadingDTO(Long id, Long productId, Long quantity, LocalDateTime createdAt) {
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
