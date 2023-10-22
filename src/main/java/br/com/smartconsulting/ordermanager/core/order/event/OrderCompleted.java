package br.com.smartconsulting.ordermanager.core.order.event;

import org.springframework.context.ApplicationEvent;

public class OrderCompleted extends ApplicationEvent {
	private static final long serialVersionUID = 432932529210532468L;
	
	private Long orderId;
	private Long userId;

	public OrderCompleted(Object source, Long orderId, Long userId) {
		super(source);
		this.orderId = orderId;
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getUserId() {
		return userId;
	}
}
