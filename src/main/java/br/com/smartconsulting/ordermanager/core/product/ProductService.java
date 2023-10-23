package br.com.smartconsulting.ordermanager.core.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface ProductService {

	@Transactional
	public ProductEntity findById(Long id);

	@Transactional
	public List<ProductEntity> findAll();

	@Transactional
	public void save(ProductEntity product);

	@Transactional
	public void delete(Long id);
}
