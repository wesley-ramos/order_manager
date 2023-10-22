package br.com.smartconsulting.ordermanager.core.stock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class StockMovementServiceImplTest {
	
	private  StockMovementService service;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private StockMovementRepository stockMovementRepository;
	
	@Mock
	private ApplicationEventPublisher publisher;
	
	@Captor
	private ArgumentCaptor<StockMovementEntity> captor;

	@BeforeEach
	public void init() {
		this.service = new StockMovementServiceImpl(stockMovementRepository, productRepository, publisher);
	}
	
	@Test
	public void whenTryingToGetAMovementThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(stockMovementRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.findById(id);
		});
	}
	
	@Test
	public void whenTheMovementExistsTheSystemShouldReturnTheMovement() {
		StockMovementEntity movement = createStockMovement(1l, 1l, 3);
			
		when(stockMovementRepository.findById(movement.getId())).thenReturn(Optional.of(movement));

		StockMovementEntity response = service.findById(movement.getId());

		verify(stockMovementRepository).findById(movement.getId());

		assertEquals(movement, response);
	}
	
	@Test
	public void whenRunningTheListItShouldReturnAllMovements() {
		StockMovementEntity cocaCola = createStockMovement(1l, 1l, 3);
		StockMovementEntity fanta = createStockMovement(2l, 2l, 5);
		
		List<StockMovementEntity> movements = new ArrayList<>();
		movements.add(cocaCola);
		movements.add(fanta);
		
		when(stockMovementRepository.findAll()).thenReturn(movements);
		
		List<StockMovementEntity> response = service.findAll();
		verify(stockMovementRepository).findAll();
		
		assertEquals(movements, response);
	}
	
	@Test
	public void whenTheMovementIsValidTheSystemShouldSaveIt() {
		ProductEntity product = createProduct(1, "Coca cola 2L");
		StockMovementEntity movement = createStockMovement(1l, product.getId(), 3);
		
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		
		service.save(movement);

		verify(stockMovementRepository).save(captor.capture());
		verify(publisher).publishEvent(any());
		
		assertEquals(captor.getValue(), movement);
	}
	
	@Test
	public void whenTryingToDeleteAMovementThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(stockMovementRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.delete(id);
		});
	}
	
	@Test
	public void whenTheMovementExistsTheSystemShouldDeleteIt() {
		StockMovementEntity movement = createStockMovement(1l, 1l, 3);
		
		when(stockMovementRepository.findById(movement.getId())).thenReturn(Optional.of(movement));
		
		service.delete(movement.getId());

		verify(stockMovementRepository).delete(captor.capture());
		
		assertEquals(captor.getValue(), movement);
	}
	
	private ProductEntity createProduct(long id, String name) {
		ProductEntity product = new  ProductEntity();
		product.setId(id);
		product.setName(name);
		return product;
	}

	private StockMovementEntity createStockMovement(long id, long productId, long quantity) {
		ProductEntity product = new ProductEntity();
		product.setId(productId);
		
		StockMovementEntity movement = new  StockMovementEntity();
		movement.setId(id);
		movement.setProduct(product);
		movement.setQuantity(quantity);
		movement.setCreatedAt(new Date());
		
		return movement;
	}
}
