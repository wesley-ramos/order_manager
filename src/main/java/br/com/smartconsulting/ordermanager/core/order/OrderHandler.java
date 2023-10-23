package br.com.smartconsulting.ordermanager.core.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.smartconsulting.ordermanager.core.order.event.OrderGeneratedEvent;
import br.com.smartconsulting.ordermanager.core.stock.events.StockMovedEvent;

@Component
public class OrderHandler {
	private Logger logger = LoggerFactory.getLogger(OrderHandler.class);
	private StockMovementAssigner assiger;

	@Autowired
	public OrderHandler(StockMovementAssigner assiger) {
		this.assiger = assiger;
	}

	@EventListener
	public void stockMoved(StockMovedEvent event) {
		logger.info("Stock was moved id {} ..", event.getStockMovementId());
		assiger.assign();
	}

	@EventListener
	public void orderGenerated(OrderGeneratedEvent event) {
		logger.info("Order id {} was generated ..", event.getOrderId());
		assiger.assign();
	}
}
