package br.com.smartconsulting.ordermanager.core.stock.events;

import org.springframework.context.ApplicationEvent;

public class StockMovedEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6309327127089432155L;
	
	private Long stockMovementId;
	private Long productId;
	private Long quantity;
	
	public StockMovedEvent(Object source, Long stockMovementId, Long productId, Long quantity) {
		super(source);
		this.stockMovementId = stockMovementId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getStockMovementId() {
		return stockMovementId;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
