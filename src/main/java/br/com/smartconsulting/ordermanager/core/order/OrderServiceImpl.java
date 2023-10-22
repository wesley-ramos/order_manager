package br.com.smartconsulting.ordermanager.core.order;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.InvalidParameterException;
import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.order.entity.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.event.OrderGeneratedEvent;
import br.com.smartconsulting.ordermanager.core.order.repository.OrderRepository;
import br.com.smartconsulting.ordermanager.core.product.ProductEntity;
import br.com.smartconsulting.ordermanager.core.product.ProductRepository;
import br.com.smartconsulting.ordermanager.core.user.UserEntity;
import br.com.smartconsulting.ordermanager.core.user.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private OrderRepository orderRepository;
	private ApplicationEventPublisher publisher;
	
	@Autowired
	public OrderServiceImpl(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository, ApplicationEventPublisher publisher) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.publisher = publisher;
	}
	
	@Override
	public OrderEntity findById(Long id) {
		return orderRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(format("Order %d was not found", id)));
	}

	@Override
	public List<OrderEntity> findAll() {
		return orderRepository.findAll();
	}
	
	@Override
	public void save(OrderEntity order) {
		UserEntity user = userRepository.findById(order.getUser().getId())
        	.orElseThrow(() -> new InvalidParameterException(format("User %d was not found", order.getUser().getId())));
		
		ProductEntity product = productRepository.findById(order.getProduct().getId())
    		.orElseThrow(() -> new InvalidParameterException(format("Product %d was not found", order.getProduct().getId())));
		
		order.setUser(user);
		order.setProduct(product);
		
		orderRepository.save(order);
		
		publisher.publishEvent(new OrderGeneratedEvent(this, order.getId()));	
	}

	@Override
	public void delete(Long id) {
		OrderEntity order = this.findById(id);
		orderRepository.delete(order);
	}
}
