package br.com.smartconsulting.ordermanager.core.order.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementEntity;
import br.com.smartconsulting.ordermanager.core.order.entities.OrderStockMovementId;

public interface OrderStockMovementRepository extends CrudRepository<OrderStockMovementEntity, OrderStockMovementId>{

}
