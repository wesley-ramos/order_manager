package br.com.smartconsulting.ordermanager.api.stock;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class StockMovementWritingDTO {
	
	@NotBlank(message = "The productId field is required")
	private Long productId;
	
	@Min(value = 1, message = "The minimum value is 1")
	@NotBlank(message = "The quantity field is required")
	private Long quantity;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}
