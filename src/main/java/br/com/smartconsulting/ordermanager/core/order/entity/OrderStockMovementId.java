package br.com.smartconsulting.ordermanager.core.order.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderStockMovementId implements Serializable {
	
	private static final long serialVersionUID = -5261550296522601798L;

	@Column(name = "order_id", insertable = false, updatable = false)
	private Long orderId;
	
	@Column(name = "stock_movement_id", insertable = false, updatable = false)
	private Long stockMovementId;
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getStockMovementId() {
		return stockMovementId;
	}

	public void setStockMovementId(Long stockMovementId) {
		this.stockMovementId = stockMovementId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, stockMovementId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderStockMovementId other = (OrderStockMovementId) obj;
		return Objects.equals(orderId, other.orderId) && Objects.equals(stockMovementId, other.stockMovementId);
	}
}
