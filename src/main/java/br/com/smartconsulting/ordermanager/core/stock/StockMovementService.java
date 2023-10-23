package br.com.smartconsulting.ordermanager.core.stock;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface StockMovementService {

	@Transactional
	public StockMovementEntity findById(Long id);

	@Transactional
	public List<StockMovementEntity> findAll();

	@Transactional
	public void save(StockMovementEntity movements);

	@Transactional
	public void delete(Long id);
}
