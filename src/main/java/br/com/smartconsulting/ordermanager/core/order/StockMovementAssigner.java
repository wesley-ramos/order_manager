package br.com.smartconsulting.ordermanager.core.order;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementId;
import br.com.smartconsulting.ordermanager.core.order.events.OrderCompleted;
import br.com.smartconsulting.ordermanager.core.order.repositories.OrderRepository;
import br.com.smartconsulting.ordermanager.core.order.repositories.OrderStockMovementRepository;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementEntity;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementRepository;

@Component
public class StockMovementAssigner {
	
	private Logger logger = LoggerFactory.getLogger(OrderListener.class);
	
	private OrderRepository orderRepository;
	private StockMovementRepository stockMovementRepository;
	private OrderStockMovementRepository orderStockMovementRepository;
	private ApplicationEventPublisher publisher;
	
	public StockMovementAssigner(
			OrderRepository orderRepository, 
			StockMovementRepository stockMovementRepository, 
			OrderStockMovementRepository orderStockMovementRepository,
			ApplicationEventPublisher publisher) {
		this.orderRepository = orderRepository;
		this.stockMovementRepository = stockMovementRepository;
		this.orderStockMovementRepository = orderStockMovementRepository;
		this.publisher = publisher;
	}
	
	@Transactional
	public void assign() {
		List<OrderEntity> orders  = orderRepository.findAllIncompleteOrders();
		
		for (OrderEntity order : orders) {
			
			long quantityAssignedForTheOrder = order.getStockMoviments()
				.stream()
				.map(moviment -> moviment.getQuantityUsed())
				.reduce(0l, Long::sum);
			
			long unassignedQuantity = order.getQuantity() - quantityAssignedForTheOrder;
			
			while (unassignedQuantity > 0) {
				StockMovementEntity stockMovement  = stockMovementRepository.findTheLastIncompleteStockMovement(order.getProduct().getId());
				if(stockMovement == null) {
					break;
				}
				
				long quantityAssignedForTheStockMovement = stockMovement.getOrders()
					.stream()
					.map(moviment -> moviment.getQuantityUsed())
					.reduce(0l, Long::sum);
				
				long quantityAvailable = stockMovement.getQuantity() - quantityAssignedForTheStockMovement;
				long quantityForAssign = unassignedQuantity >= quantityAvailable ? quantityAvailable: unassignedQuantity;
				unassignedQuantity = unassignedQuantity - quantityForAssign;

				OrderStockMovementId identifier = new OrderStockMovementId();
				identifier.setOrderId(order.getId());
				identifier.setStockMovementId(stockMovement.getId());
				
				OrderStockMovementEntity orderMovement = new OrderStockMovementEntity();
				orderMovement.setId(identifier);
				orderMovement.setQuantityUsed(quantityForAssign);
				orderMovement.setOrder(order);
				orderMovement.setStockMovement(stockMovement);
				
				orderStockMovementRepository.save(orderMovement);
				
				stockMovement.addOrder(orderMovement);
				stockMovementRepository.save(stockMovement);
				order.addMoviment(orderMovement);
							
				logger.info("Assigning stock movement {} to order {} with the quantity of {} items", stockMovement.getId(), order.getId(), quantityForAssign);
			}
			
			orderRepository.save(order);
			
			if(order.isCompleted()) {
				publisher.publishEvent(new OrderCompleted(this, order.getId(), order.getUser().getId()));
			}
		}
	}
}
