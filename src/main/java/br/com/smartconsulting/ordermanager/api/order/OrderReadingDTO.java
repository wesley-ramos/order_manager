package br.com.smartconsulting.ordermanager.api.order;

import java.time.LocalDateTime;

public class OrderReadingDTO {
	private Long id;
	private Long productId;
	private Long userId;
	private Long quantity; 
	private boolean completed;
	private LocalDateTime createdAt;
	
	public OrderReadingDTO(Long id, Long productId, Long userId, Long quantity, boolean completed, LocalDateTime createdAt){
		this.id = id;
		this.productId = productId;
		this.userId = userId;
		this.quantity = quantity;
		this.completed = completed;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public boolean isCompleted() {
		return completed;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
