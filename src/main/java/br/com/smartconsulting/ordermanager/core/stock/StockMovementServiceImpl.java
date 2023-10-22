package br.com.smartconsulting.ordermanager.core.stock;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.product.ProductRepository;
import br.com.smartconsulting.ordermanager.core.stock.events.StockMovedEvent;

@Service
public class StockMovementServiceImpl implements StockMovementService {
	
	private ProductRepository productRepository;
	private StockMovementRepository stockMovementRepository;
	private ApplicationEventPublisher publisher;

	@Autowired
	public StockMovementServiceImpl(StockMovementRepository repository, ProductRepository productRepository, ApplicationEventPublisher publisher) {
		this.stockMovementRepository = repository;
		this.productRepository = productRepository;
		this.publisher = publisher;
	}
	
	@Override
	public StockMovementEntity findById(Long id) {
		return stockMovementRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(format("StockMovement %d was not found", id)));
	}

	@Override
	public List<StockMovementEntity> findAll() {
		return stockMovementRepository.findAll();
	}

	@Override
	public void save(StockMovementEntity movement) {
		ProductEntity product = productRepository.findById(movement.getProduct().getId())
				.orElseThrow(() -> new NotFoundException(format("Product %d was not found", movement.getProduct().getId())));
		
		movement.setProduct(product);
		stockMovementRepository.save(movement);
		
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
		stockMovementRepository.delete(movement);
	}
}
