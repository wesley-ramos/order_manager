package br.com.smartconsulting.ordermanager.core.order.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.smartconsulting.ordermanager.core.order.entity.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.order.entity.OrderStockMovementId;

public interface OrderStockMovementRepository extends CrudRepository<OrderStockMovementEntity, OrderStockMovementId>{

}
