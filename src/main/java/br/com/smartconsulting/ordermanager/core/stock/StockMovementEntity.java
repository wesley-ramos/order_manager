package br.com.smartconsulting.ordermanager.core.stock;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.smartconsulting.ordermanager.core.order.entity.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;

@Entity
@Table(name = "stock_movements")
public class StockMovementEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductEntity product;
	
	private Long quantity;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@OneToMany
	@JoinColumn(name = "stock_movement_id")
	private Set<OrderStockMovementEntity> orders;
	
	private boolean available;
	
	public StockMovementEntity() {
		this.orders = new HashSet<>();
		this.available = true;
		this.createdAt = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set<OrderStockMovementEntity> getOrders() {
		return orders;
	}

	public void addOrder(OrderStockMovementEntity order) {
		this.orders.add(order);
		
		Long totalAmountUsed = orders
			.stream()
			.map(moviment -> moviment.getQuantityUsed())
			.reduce(0l, Long::sum);
		
		if (quantity > totalAmountUsed) {
			available = true;
			return;
		}
		
		available = false;
	}

	public boolean isAvailable() {
		return available;
	}
}
