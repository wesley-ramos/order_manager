package br.com.smartconsulting.ordermanager.core.order;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementId;
import br.com.smartconsulting.ordermanager.core.order.repositories.OrderRepository;
import br.com.smartconsulting.ordermanager.core.order.repositories.OrderStockMovementRepository;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementEntity;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementRepository;

@Component
public class OrderStockMovementAssigner {
	
	private Logger logger = LoggerFactory.getLogger(OrderListener.class);
	private OrderRepository orderRepository;
	private StockMovementRepository stockMovementRepository;
	private OrderStockMovementRepository orderStockMovementRepository;
	
	public OrderStockMovementAssigner(
			OrderRepository orderRepository, 
			StockMovementRepository stockMovementRepository, 
			OrderStockMovementRepository orderStockMovementRepository) {
		this.orderRepository = orderRepository;
		this.stockMovementRepository = stockMovementRepository;
		this.orderStockMovementRepository = orderStockMovementRepository;
	}
	
	@Transactional
	public void assign() {
		List<OrderEntity> orders  = orderRepository.findIncomplete();
		
		for (OrderEntity order : orders) {
			
			long quantityMovimented = order.getStockMoviments()
				.stream()
				.map(moviment -> moviment.getQuantityUsed())
				.reduce(0l, Long::sum);
			
			long remaining = order.getQuantity() - quantityMovimented;
			
			while (remaining > 0) {
				StockMovementEntity stockMovement  = stockMovementRepository.findTheLastAvailable(order.getProduct().getId());
				
				if(stockMovement == null) {
					break;
				}
				
				long assignedQuantity = stockMovement.getOrders()
					.stream()
					.map(moviment -> moviment.getQuantityUsed())
					.reduce(0l, Long::sum);
				
				long quantityAvailable = stockMovement.getQuantity() - assignedQuantity;
				long quantityUsed = remaining >= quantityAvailable ? quantityAvailable: remaining;
				remaining = remaining - quantityUsed;

				OrderStockMovementId identifier = new OrderStockMovementId();
				identifier.setOrderId(order.getId());
				identifier.setStockMovementId(stockMovement.getId());
				
				OrderStockMovementEntity orderMovement = new OrderStockMovementEntity();
				orderMovement.setId(identifier);
				orderMovement.setQuantityUsed(quantityUsed);
				orderMovement.setOrder(order);
				orderMovement.setStockMovement(stockMovement);
				
				orderStockMovementRepository.save(orderMovement);
				
				stockMovement.addOrder(orderMovement);
				stockMovementRepository.save(stockMovement);
				
				order.addMoviment(orderMovement);
				orderRepository.save(order);
				
				logger.info("Assigning stock movement {} to order {} with the quantity of {} items", stockMovement.getId(), order.getId(), quantityUsed);
			}
		}
	}
}
