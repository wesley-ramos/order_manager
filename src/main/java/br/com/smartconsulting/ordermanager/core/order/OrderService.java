package br.com.smartconsulting.ordermanager.core.order;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;

public interface OrderService {
	
	@Transactional
    public OrderEntity findById(Long id);
	
	@Transactional
    public List<OrderEntity> findAll();
	
	@Transactional
    public void save(OrderEntity order);
	
	@Transactional
    public void delete(Long id);
}
