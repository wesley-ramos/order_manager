package br.com.smartconsulting.ordermanager.core.product;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository repository;

	@Autowired
	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public ProductEntity findById(Long id) {
		return repository.findById(id)
            .orElseThrow(() -> new NotFoundException(format("Product %d was not found", id)));
	}

	@Override
	public List<ProductEntity> findAll() {
		return repository.findAll();
	}

	@Override
	public void save(ProductEntity product) {
		repository.save(product);
	}
	
	//TODO Do not allow to delete when there is a stock movement or an order
	@Override
	public void delete(Long id) {
		ProductEntity product = this.findById(id);
		repository.delete(product);
	}
}
