package br.com.smartconsulting.ordermanager.core.stock;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface StockMovementRepository extends CrudRepository<StockMovementEntity, Long> {
	public List<StockMovementEntity> findAll();
	
	@Query(value = "SELECT sm.* FROM stock_movements sm WHERE sm.available = true and sm.product_id = ?1 ORDER BY sm.created_at ASC LIMIT 1", nativeQuery = true)
	public StockMovementEntity findTheLastIncompleteStockMovement(Long productId);
	
	@Query(value = "SELECT count(id) FROM stock_movements WHERE product_id = ?1", nativeQuery = true)
	Long countByProductId(Long productId);
}
