package br.com.smartconsulting.ordermanager.core.stock;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.stock.events.StockMovedEvent;

@Service
public class StockMovementServiceImpl implements StockMovementService {
	
	private StockMovementRepository repository;
	private ApplicationEventPublisher publisher;

	@Autowired
	public StockMovementServiceImpl(StockMovementRepository repository, ApplicationEventPublisher publisher) {
		this.repository = repository;
		this.publisher = publisher;
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
	public void save(StockMovementEntity movement) {
		repository.save(movement);
		
		StockMovedEvent event = new StockMovedEvent(
			this,
			movement.getId(),
			movement.getProduct().getId(),
			movement.getQuantity()
		);
		
		publisher.publishEvent(event);
	}

	@Override
	public void delete(Long id) {
		StockMovementEntity movement = this.findById(id);
		repository.delete(movement);
	}
}
