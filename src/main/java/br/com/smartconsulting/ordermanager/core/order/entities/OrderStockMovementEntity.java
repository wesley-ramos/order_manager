package br.com.smartconsulting.ordermanager.core.order.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import br.com.smartconsulting.ordermanager.core.stock.StockMovementEntity;

@Entity
@Table(name = "order_stock_movements")
public class OrderStockMovementEntity {
	
	@EmbeddedId
	private OrderStockMovementId id;
	
	@ManyToOne
	@MapsId("orderId")
	private OrderEntity order;
	
	@ManyToOne
	@MapsId("stockMovementId")
	private StockMovementEntity stockMovement;
	
	@Column(name = "quantity_used")
	private Long quantityUsed;

	public OrderStockMovementId getId() {
		return id;
	}

	public void setId(OrderStockMovementId id) {
		this.id = id;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public StockMovementEntity getStockMovement() {
		return stockMovement;
	}

	public void setStockMovement(StockMovementEntity stockMovement) {
		this.stockMovement = stockMovement;
	}

	public Long getQuantityUsed() {
		return quantityUsed;
	}

	public void setQuantityUsed(Long quantityUsed) {
		this.quantityUsed = quantityUsed;
	}
}
