package br.com.smartconsulting.ordermanager.api.order;

import java.time.LocalDateTime;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderStatus;

public class OrderReadingDTO {
	private Long id;
	private Long productId;
	private Long userId;
	private Long quantity; 
	private OrderStatus status;
	private LocalDateTime createdAt;
	
	public OrderReadingDTO(Long id, Long productId, Long userId, Long quantity, OrderStatus status, LocalDateTime createdAt){
		this.id = id;
		this.productId = productId;
		this.userId = userId;
		this.quantity = quantity;
		this.status = status;
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

	public OrderStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
