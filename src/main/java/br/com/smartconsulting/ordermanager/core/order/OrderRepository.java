package br.com.smartconsulting.ordermanager.core.order;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
	public List<OrderEntity> findAll();
}
