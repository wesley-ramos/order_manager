package br.com.smartconsulting.ordermanager.core.order.notifier;

import static java.lang.String.format;

import java.io.StringWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.common.mail.EmailService;
import br.com.smartconsulting.ordermanager.core.order.entity.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.entity.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.order.event.OrderCompleted;
import br.com.smartconsulting.ordermanager.core.order.repository.OrderRepository;

@Service
public class CompletedOrderNotifier{
	
	private Logger logger = LoggerFactory.getLogger(CompletedOrderNotifier.class);
	
	private OrderRepository repository;
	private VelocityEngine velocityEngine;
	private EmailService emailService;
	
	@Autowired
	public CompletedOrderNotifier(
			OrderRepository repository, 
			VelocityEngine velocityEngine,
			EmailService emailService) {
		this.repository = repository;
		this.velocityEngine = velocityEngine;
		this.emailService = emailService;
	}
	
	@EventListener
	@Transactional
	public void onCompleted(OrderCompleted event){
		logger.info("Order {} completed ....", event.getOrderId());
		logger.info("Notifying the user {} ", event.getUserId());
		
		OrderEntity order = repository.findById(event.getOrderId())
			.orElseThrow(() -> new NotFoundException(format("Order %d was not found", event.getOrderId())));
		  
        Template template = velocityEngine.getTemplate("templates/order.vm");   
        VelocityContext context = new VelocityContext();
        
        Comparator<OrderStockMovementEntity> comparator = Comparator.comparing(movement -> movement.getStockMovement().getId());
        List<OrderStockMovementEntity> stockMovements = order.getStockMoviments().stream()
    		.sorted(comparator)
    		.collect(Collectors.toList());
        
        context.put("orderId", order.getId());
        context.put("userName", order.getUser().getName());
        context.put("productId", order.getProduct().getId());
        context.put("productName", order.getProduct().getName());
        context.put("productQuantity", order.getQuantity());
        context.put("stockMovements", stockMovements);
        
        StringWriter body = new StringWriter();
        template.merge(context, body);
        
        emailService.send(
    		format("Order %d was completed", order.getId()), 
    		body.toString(), 
    		order.getUser().getEmail()
		);
	}
}
