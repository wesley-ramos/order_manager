package br.com.smartconsulting.ordermanager.core.order.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.user.UserEntity;

@Entity
@Table(name = "orders")
public class OrderEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity product;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	private Long quantity;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@OneToMany
	@JoinColumn(name = "order_id")
	private Set<OrderStockMovementEntity> stockMoviments;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	public OrderEntity() {
		this.stockMoviments = new HashSet<>();
		this.status = OrderStatus.PENDING;
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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Set<OrderStockMovementEntity> getStockMoviments() {
		return stockMoviments;
	}

	public void setStockMoviments(Set<OrderStockMovementEntity> stockMoviments) {
		this.stockMoviments = stockMoviments;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
