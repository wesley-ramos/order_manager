package br.com.smartconsulting.ordermanager.core.stock;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;

@Service
public class StockMovementServiceImpl implements StockMovementService {
	
	private StockMovementRepository repository;

	@Autowired
	public StockMovementServiceImpl(StockMovementRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public StockMovementEntity findById(Long id) {
		return repository.findById(id)
            .orElseThrow(() -> new NotFoundException(format("StockMovement %d was not found", id)));
	}

	@Override
	public List<StockMovementEntity> findAll() {
		return repository.findAll();
	}

	@Override
	public void save(StockMovementEntity movements) {
		repository.save(movements);
	}

	@Override
	public void delete(Long id) {
		StockMovementEntity movement = this.findById(id);
		repository.delete(movement);
	}
}
