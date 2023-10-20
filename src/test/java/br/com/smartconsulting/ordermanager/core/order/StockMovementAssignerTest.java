package br.com.smartconsulting.ordermanager.core.order;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.order.repositories.OrderRepository;
import br.com.smartconsulting.ordermanager.core.order.repositories.OrderStockMovementRepository;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementEntity;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementRepository;
import br.com.smartconsulting.ordermanager.core.user.UserEntity;

@ExtendWith(MockitoExtension.class)
public class StockMovementAssignerTest {
		
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private StockMovementRepository stockMovementRepository;
	@Mock
	private OrderStockMovementRepository orderStockMovementRepository;
	
	@Mock
	private ApplicationEventPublisher publisher;
	
	@Captor
	private ArgumentCaptor<OrderEntity> orderCaptor;
	
	@Captor
	private ArgumentCaptor<StockMovementEntity> stockMovementCaptor;
	
	private StockMovementAssigner assigner;
	
	@BeforeEach
	public void init() {
		this.assigner = new StockMovementAssigner(
			orderRepository, 
			stockMovementRepository, 
			orderStockMovementRepository,
			publisher
		);
	}
	
	@Test
	public void whenThereIsNotEnoughStockMovementTheOrderShouldBeIncomplete() {
		OrderEntity order = createOrder(1, 2, 3, 35, new HashSet<OrderStockMovementEntity>());
		List<OrderEntity> orders = new ArrayList<>();
		orders.add(order);
		
		when(orderRepository.findAllIncompleteOrders()).thenReturn(orders);
		
		StockMovementEntity stockMovement1 = createStockMovement(1, 1, 5);
		StockMovementEntity stockMovement2 = createStockMovement(2, 1, 10);
		StockMovementEntity stockMovement3 = createStockMovement(3, 1, 15);
		
		when(stockMovementRepository.findTheLastIncompleteStockMovement(order.getProduct().getId()))
			.thenReturn(stockMovement1, stockMovement2, stockMovement3, null);
		
		assigner.assign();
		
		verify(orderRepository).save(orderCaptor.capture());
		verify(stockMovementRepository, times(3)).save(any());
		verify(orderStockMovementRepository, times(3)).save(any());
		
		OrderEntity orderSaved = orderCaptor.getValue();
		
		assertThat(orderSaved.getStockMoviments(), hasSize(3));
		
		assertThat(orderSaved.getStockMoviments(),
			hasItems(
				hasProperty("quantityUsed", Matchers.equalTo(5l)),
				hasProperty("quantityUsed", Matchers.equalTo(10l)),
				hasProperty("quantityUsed", Matchers.equalTo(15l))
			)
		);
		
		assertFalse(orderSaved.isCompleted());
	}
	
	@Test
	public void whenThereIsEnoughStockMovementTheOrderShouldBeComplete() {
		OrderEntity order = createOrder(1, 2, 3, 10, new HashSet<OrderStockMovementEntity>());
		List<OrderEntity> orders = new ArrayList<>();
		orders.add(order);
		
		when(orderRepository.findAllIncompleteOrders()).thenReturn(orders);
		
		StockMovementEntity stockMovement = createStockMovement(1, 1, 20);
		
		when(stockMovementRepository.findTheLastIncompleteStockMovement(order.getProduct().getId()))
			.thenReturn(stockMovement);
		
		assigner.assign();
		
		verify(orderRepository).save(orderCaptor.capture());
		verify(stockMovementRepository, times(1)).save(any());
		verify(orderStockMovementRepository, times(1)).save(any());
		
		verify(publisher).publishEvent(any());
		
		OrderEntity orderSaved = orderCaptor.getValue();
		
		assertThat(orderSaved.getStockMoviments(), hasSize(1));
		
		assertThat(orderSaved.getStockMoviments(),
			hasItems(
				hasProperty("quantityUsed", Matchers.equalTo(10l))
			)
		);
		
		assertTrue(orderSaved.isCompleted());
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
	
	private OrderEntity createOrder(long id, long productId, long userId, long quantity, Set<OrderStockMovementEntity> moviments) {
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
}
