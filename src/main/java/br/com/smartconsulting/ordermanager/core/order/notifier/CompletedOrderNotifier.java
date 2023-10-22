package br.com.smartconsulting.ordermanager.core.order.notifier;

import static java.lang.String.format;

import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
import br.com.smartconsulting.ordermanager.core.order.entity.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.event.OrderCompleted;
import br.com.smartconsulting.ordermanager.core.order.repository.OrderRepository;

@Service
public class CompletedOrderNotifier{
	
	private Logger logger = LoggerFactory.getLogger(CompletedOrderNotifier.class);
	
	private OrderRepository repository;
	private VelocityEngine velocityEngine;
	private SMTPConfiguration config;
	
	@Autowired
	public CompletedOrderNotifier(OrderRepository repository, 
			VelocityEngine velocityEngine,
			SMTPConfiguration config) {
		this.repository = repository;
		this.velocityEngine = velocityEngine;
		this.config = config;
	}
	
	@EventListener
	@Transactional
	public void onCompleted(OrderCompleted event) throws Exception {
		if (!config.isConfigured()) {
			return;
		}
		
		logger.info("Order {} completed ....", event.getOrderId());
		logger.info("Notifying the user {} ", event.getUserId());
		
		try {
			OrderEntity order = repository.findById(event.getOrderId())
				.orElseThrow(() -> new NotFoundException(format("Order %d was not found", event.getOrderId())));
			  
	        Template template = velocityEngine.getTemplate("templates/order.vm");   
	        VelocityContext context = new VelocityContext();
	        
	        context.put("orderId", order.getId());
	        context.put("userName", order.getUser().getName());
	        context.put("productId", order.getProduct().getId());
	        context.put("productName", order.getProduct().getName());
	        context.put("productQuantity", order.getQuantity());
	        context.put("stockMovements", order.getStockMoviments());
	        
	        StringWriter body = new StringWriter();
	        template.merge(context, body);
	        
	        Properties prop = new Properties();
			prop.put("mail.smtp.host", config.getHost());
	        prop.put("mail.smtp.port", config.getPort());
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.starttls.enable", "true");
	        
	        Session session = Session.getInstance(prop,
	        new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(config.getEmail(), config.getPassword());
	            }
	        });
	        
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(config.getEmail()));
	        message.setRecipients(
	            Message.RecipientType.TO,
	            InternetAddress.parse(order.getUser().getEmail())
	        );
	        message.setSubject(format("Order %d was completed", order.getId()));
	        message.setContent(body.toString(), "text/html; charset=utf-8");
	        Transport.send(message);
		}catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}
}
