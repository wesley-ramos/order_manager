package br.com.smartconsulting.ordermanager.core.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import br.com.smartconsulting.ordermanager.core.common.exceptions.InvalidParameterException;
import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementId;
import br.com.smartconsulting.ordermanager.core.order.repositories.OrderRepository;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.product.ProductRepository;
import br.com.smartconsulting.ordermanager.core.user.UserEntity;
import br.com.smartconsulting.ordermanager.core.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
	
	private  OrderService service;

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private ApplicationEventPublisher publisher;
	
	@Captor
	private ArgumentCaptor<OrderEntity> captor;
	
	@BeforeEach
	public void init() {
		this.service = new OrderServiceImpl(
			userRepository, 
			productRepository, 
			orderRepository,
			publisher
		);
	}
	
	@Test
	public void whenTryingToGetAOrderThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(orderRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.findById(id);
		});
	}
	
	@Test
	public void whenTheOrderExistsTheSystemShouldReturnTheOrder() {
		
		OrderEntity order = createEntity(1l, 1, 1, 3, new HashSet<OrderStockMovementEntity>());
			
		when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

		OrderEntity response = service.findById(order.getId());

		verify(orderRepository).findById(order.getId());

		assertEquals(order, response);
	}
	
	@Test
	public void whenRunningTheListItShouldReturnAllOrders() {
		OrderEntity order1 = createEntity(1l, 1, 1, 3, new HashSet<OrderStockMovementEntity>());
		OrderEntity order2 = createEntity(2l, 1, 1, 6, new HashSet<OrderStockMovementEntity>());
		
		List<OrderEntity> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);
		
		when(orderRepository.findAll()).thenReturn(orders);
		
		List<OrderEntity> response = service.findAll();
		verify(orderRepository).findAll();
		
		assertEquals(orders, response);
	}
	
	@Test
	public void whenCreatingAnOrderWithAnInvalidUserTheSystemShouldThrowAnException() {
		OrderEntity order = createEntity(2l, 29, 1, 2, new HashSet<OrderStockMovementEntity>());
		
		when(userRepository.findById(order.getUser().getId())).thenReturn(Optional.empty());
		
		Exception exception = assertThrows(InvalidParameterException.class, () -> {
			service.save(order);
		});
		
		assertEquals("User 1 was not found", exception.getMessage());
	}
	
	@Test
	public void whenCreatingAnOrderWithAnInvalidProductTheSystemShouldThrowAnException() {
		OrderEntity order = createEntity(2l, 29, 1, 2, new HashSet<OrderStockMovementEntity>());
		
		when(userRepository.findById(order.getUser().getId())).thenReturn(Optional.of(order.getUser()));
		when(productRepository.findById(order.getProduct().getId())).thenReturn(Optional.empty());
		
		Exception exception = assertThrows(InvalidParameterException.class, () -> {
			service.save(order);
		});
		
		assertEquals("Product 29 was not found", exception.getMessage());
	}
	
	@Test
	public void whenTheOrderDoesNotHaveAllTheNecessaryMovementsTheSystemShouldSaveItIncomplete() {
		OrderEntity order = createEntity(2l, 1, 1, 6, new HashSet<OrderStockMovementEntity>());
		
		when(userRepository.findById(order.getUser().getId())).thenReturn(Optional.of(order.getUser()));
		when(productRepository.findById(order.getProduct().getId())).thenReturn(Optional.of(order.getProduct()));
		
		service.save(order);

		verify(orderRepository).save(captor.capture());
		
		verify(publisher).publishEvent(any());
		
		assertFalse(captor.getValue().isCompleted());
	}
	
	@Test
	public void whenTheOrderHasAllTheNecessaryMovementsTheSystemShouldSaveItCompleted() {
		OrderStockMovementEntity movement = createMovement(2, 1, 6);
		Set<OrderStockMovementEntity> moviments = new HashSet<OrderStockMovementEntity>();
		moviments.add(movement);
		
		OrderEntity order = createEntity(2l, 1, 1, 6, moviments);
		
		when(userRepository.findById(order.getUser().getId())).thenReturn(Optional.of(order.getUser()));
		when(productRepository.findById(order.getProduct().getId())).thenReturn(Optional.of(order.getProduct()));
		
		service.save(order);

		verify(orderRepository).save(captor.capture());
		verify(publisher, times(2)).publishEvent(any());
		
		assertTrue(captor.getValue().isCompleted());
	}
	
	@Test
	public void whenTryingToDeleteAOrderThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(orderRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.delete(id);
		});
	}
	
	@Test
	public void whenTheOrderExistsTheSystemShouldDeleteIt() {
		OrderEntity order = createEntity(2l, 1, 1, 6, new HashSet<OrderStockMovementEntity>());
		
		when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
		
		service.delete(order.getId());

		verify(orderRepository).delete(captor.capture());
		
		assertEquals(captor.getValue(), order);
	}

	private OrderEntity createEntity(long id, long productId, long userId, long quantity, Set<OrderStockMovementEntity> moviments) {
		ProductEntity product = new ProductEntity();
		product.setId(productId);
		
		UserEntity user = new UserEntity();
		user.setId(userId);
		
		OrderEntity order = new  OrderEntity();
		order.setId(id);
		order.setUser(user);
		order.setProduct(product);
		order.setQuantity(quantity);
		
		for (OrderStockMovementEntity movement : moviments) {
			order.addMoviment(movement);
		}
		
		return order;
	}
	
	private OrderStockMovementEntity createMovement(long orderId, long stockMovementId, long quantity) {
		OrderStockMovementId identifier = new OrderStockMovementId();
		identifier.setOrderId(orderId);
		identifier.setStockMovementId(stockMovementId);
		
		OrderStockMovementEntity entity = new OrderStockMovementEntity();
		entity.setId(identifier);
		entity.setQuantityUsed(quantity);
		return entity;
	}
}
