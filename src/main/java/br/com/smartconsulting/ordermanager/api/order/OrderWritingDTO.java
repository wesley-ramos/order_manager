package br.com.smartconsulting.ordermanager.api.order;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderWritingDTO {
	
	@NotNull(message = "The productId field is required")
	private Long productId;
	
	@NotNull(message = "The userId field is required")
	private Long userId;
	
	@Min(value = 1, message = "The minimum value is 1")
	@NotNull(message = "The quantity field is required")
	private Long quantity;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	} 
}
