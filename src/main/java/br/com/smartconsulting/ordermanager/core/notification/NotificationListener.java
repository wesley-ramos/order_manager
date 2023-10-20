package br.com.smartconsulting.ordermanager.core.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.smartconsulting.ordermanager.core.order.events.OrderCompleted;

@Component
public class NotificationListener {
	
	private Logger logger = LoggerFactory.getLogger(NotificationListener.class);
	
	@EventListener
    public void orderCompleted(OrderCompleted event) {
		logger.info("Order {} completed ....", event.getOrderId());
		logger.info("Notifying the user {} ", event.getUserId());
    }
}
