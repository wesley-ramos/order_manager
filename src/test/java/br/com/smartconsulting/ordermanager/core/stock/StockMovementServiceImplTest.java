package br.com.smartconsulting.ordermanager.core.stock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;

@ExtendWith(MockitoExtension.class)
public class StockMovementServiceImplTest {
	
	private  StockMovementService service;

	@Mock
	private StockMovementRepository repository;
	
	@Captor
	private ArgumentCaptor<StockMovementEntity> captor;

	@BeforeEach
	public void init() {
		this.service = new StockMovementServiceImpl(repository);
	}
	
	@Test
	public void whenTryingToGetAMovementThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.findById(id);
		});
	}
	
	@Test
	public void whenTheMovementExistsTheSystemShouldReturnTheMovement() {
		StockMovementEntity movement = createEntity(1l, 1l, 3);
			
		when(repository.findById(movement.getId())).thenReturn(Optional.of(movement));

		StockMovementEntity response = service.findById(movement.getId());

		verify(repository).findById(movement.getId());

		assertEquals(movement, response);
	}
	
	@Test
	public void whenRunningTheListItShouldReturnAllMovements() {
		StockMovementEntity cocaCola = createEntity(1l, 1l, 3);
		StockMovementEntity fanta = createEntity(2l, 2l, 5);
		
		List<StockMovementEntity> movements = new ArrayList<>();
		movements.add(cocaCola);
		movements.add(fanta);
		
		when(repository.findAll()).thenReturn(movements);
		
		List<StockMovementEntity> response = service.findAll();
		verify(repository).findAll();
		
		assertEquals(movements, response);
	}
	
	@Test
	public void whenTheMovementIsValidTheSystemShouldSaveIt() {
		StockMovementEntity movement = createEntity(1l, 1l, 3);
		
		service.save(movement);

		verify(repository).save(captor.capture());
		
		assertEquals(captor.getValue(), movement);
	}
	
	@Test
	public void whenTryingToDeleteAMovementThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.delete(id);
		});
	}
	
	@Test
	public void whenTheMovementExistsTheSystemShouldDeleteIt() {
		StockMovementEntity movement = createEntity(1l, 1l, 3);
		
		when(repository.findById(movement.getId())).thenReturn(Optional.of(movement));
		
		service.delete(movement.getId());

		verify(repository).delete(captor.capture());
		
		assertEquals(captor.getValue(), movement);
	}

	private StockMovementEntity createEntity(long id, long productId, long quantity) {
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
