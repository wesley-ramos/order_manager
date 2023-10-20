package br.com.smartconsulting.ordermanager.core.order;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.InvalidParameterException;
import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStatus;
import br.com.smartconsulting.ordermanager.core.product.ProductRepository;
import br.com.smartconsulting.ordermanager.core.user.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private OrderRepository orderRepository;
	
	@Autowired
	public OrderServiceImpl(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
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
		userRepository.findById(order.getUser().getId())
        	.orElseThrow(() -> new InvalidParameterException(format("User %d was not found", order.getUser().getId())));
		
		productRepository.findById(order.getProduct().getId())
    		.orElseThrow(() -> new InvalidParameterException(format("Product %d was not found", order.getProduct().getId())));
		
		Long quantityMovimented = order.getStockMoviments()
			.stream()
			.map(moviment -> moviment.getQuantityUsed())
			.reduce(0l, Long::sum);
		
		if (order.getQuantity().equals(quantityMovimented)) {
			order.setStatus(OrderStatus.COMPLETED);
		}else {
			order.setStatus(OrderStatus.PENDING);
		}
		
		orderRepository.save(order);
	}

	@Override
	public void delete(Long id) {
		OrderEntity order = this.findById(id);
		orderRepository.delete(order);
	}
}
