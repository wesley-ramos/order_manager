package br.com.smartconsulting.ordermanager.core.stock;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface StockMovementRepository extends CrudRepository<StockMovementEntity, Long> {
	public List<StockMovementEntity> findAll();
	
	@Query(value = "SELECT sm.* FROM stock_movements sm WHERE sm.available = true and sm.product_id = ?1 ORDER BY sm.created_at ASC LIMIT 1", nativeQuery = true)
	public StockMovementEntity findTheLastAvailable(Long productId);
}
