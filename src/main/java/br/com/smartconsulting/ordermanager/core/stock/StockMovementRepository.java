package br.com.smartconsulting.ordermanager.core.stock;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface StockMovementRepository extends CrudRepository<StockMovementEntity, Long> {
	public List<StockMovementEntity> findAll();
}
