package br.com.smartconsulting.ordermanager.core.order.events;

import org.springframework.context.ApplicationEvent;

public class OrderCompleted extends ApplicationEvent {
	private static final long serialVersionUID = 432932529210532468L;
	
	private Long orderId;

	public OrderCompleted(Object source, Long orderId) {
		super(source);
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return orderId;
	}
}
