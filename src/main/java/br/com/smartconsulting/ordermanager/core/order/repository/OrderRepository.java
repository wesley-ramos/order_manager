package br.com.smartconsulting.ordermanager.core.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.smartconsulting.ordermanager.core.order.entity.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
	public List<OrderEntity> findAll();
	
	@Query(value = "SELECT o FROM OrderEntity o WHERE o.completed = false ORDER BY o.createdAt asc")
	public List<OrderEntity> findAllIncompleteOrders();
	
	@Query(value = "SELECT count(id) FROM orders WHERE user_id = ?1", nativeQuery = true)
	Long countByUserId(Long userId);
	
	@Query(value = "SELECT count(id) FROM orders WHERE product_id = ?1", nativeQuery = true)
	Long countByProductId(Long productId);
}
