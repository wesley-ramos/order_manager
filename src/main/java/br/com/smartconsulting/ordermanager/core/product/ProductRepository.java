package br.com.smartconsulting.ordermanager.core.product;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
	public List<ProductEntity> findAll();
}
