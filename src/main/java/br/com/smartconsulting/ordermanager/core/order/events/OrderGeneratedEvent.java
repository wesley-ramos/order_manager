package br.com.smartconsulting.ordermanager.core.order.events;

import org.springframework.context.ApplicationEvent;

public class OrderGeneratedEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 3463237422886389950L;
	private Long orderId;
	
	public OrderGeneratedEvent(Object source, Long orderId) {
		super(source);
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return orderId;
	}
}
