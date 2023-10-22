package br.com.smartconsulting.ordermanager.core.order.notifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.common.mail.EmailService;
import br.com.smartconsulting.ordermanager.core.order.entity.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.event.OrderCompleted;
import br.com.smartconsulting.ordermanager.core.order.repository.OrderRepository;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.user.UserEntity;

@ExtendWith(MockitoExtension.class)
public class CompletedOrderNotifierTest {
	
	@Mock
	private OrderRepository repository;
	
	@Mock
	private VelocityEngine velocityEngine;
	
	@Mock
	private Template template;
	
	@Mock
	private EmailService emailService;
	
	@Captor
	private ArgumentCaptor<String> subjectCaptor;
	
	@Captor
	private ArgumentCaptor<String> contentCaptor;
	
	@Captor
	private ArgumentCaptor<String> emailCaptor;
	
	private CompletedOrderNotifier notifier;
	
	@BeforeEach
	public void init() {
		this.notifier = new CompletedOrderNotifier(
			repository, 
			velocityEngine, 
			emailService
		);
	}
	
	public void whenTheOrderIsNotFoundTheSystemShouldThrowAnException() {
		UserEntity user = new UserEntity();
		user.setId(1l);
		
		
		OrderEntity order = new  OrderEntity();
		order.setId(3l);
		order.setUser(user);
		
		when(repository.findById(order.getId())).thenReturn(Optional.empty());
		
		OrderCompleted event = new OrderCompleted(this, order.getId(), order.getUser().getId());
		
		assertThrows(NotFoundException.class, () -> {
			notifier.onCompleted(event);
		});
	}
	
	@Test
	public void whenAnOrderIsCompletedTheUserShouldBeNotified() {
		ProductEntity product = new ProductEntity();
		product.setId(1l);
		product.setName("Coca cola 2L");
		
		UserEntity user = new UserEntity();
		user.setId(1l);
		user.setName("Wesley Ramos");
		user.setEmail("wesley.ramos.developer@gmail.com");
		
		OrderEntity order = new  OrderEntity();
		order.setId(1l);
		order.setUser(user);
		order.setProduct(product);
		order.setQuantity(10l);
		
		when(repository.findById(order.getId())).thenReturn(Optional.of(order));
		when(velocityEngine.getTemplate(anyString())).thenReturn(template);
		
		OrderCompleted event = new OrderCompleted(this, order.getId(), user.getId());
		notifier.onCompleted(event);
		
		verify(emailService).send(subjectCaptor.capture(), contentCaptor.capture(), emailCaptor.capture());
		
		assertNotNull(subjectCaptor.getValue());
		assertNotNull(contentCaptor.getValue());
		assertEquals(user.getEmail(), emailCaptor.getValue());
	}
}
