package br.com.smartconsulting.ordermanager.core.product;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.InvalidOperationException;
import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.order.repository.OrderRepository;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private OrderRepository orderRepository;
	private StockMovementRepository stockMovementRepository;

	@Autowired
	public ProductServiceImpl(
			ProductRepository productRepository, 
			OrderRepository orderRepository,
			StockMovementRepository stockMovementRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.stockMovementRepository = stockMovementRepository;
	}

	@Override
	public ProductEntity findById(Long id) {
		return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(format("Product %d was not found", id)));
	}

	@Override
	public List<ProductEntity> findAll() {
		return productRepository.findAll();
	}

	@Override
	public void save(ProductEntity product) {
		productRepository.save(product);
	}
	
	@Override
	public void delete(Long id) {
		ProductEntity product = this.findById(id);
		
		Long orders = orderRepository.countByProductId(product.getId());
		
		if (orders > 0) {
			throw new InvalidOperationException("It was not possible to delete this product, as there are orders linked to this product");
		}
		
		Long stockMovements = stockMovementRepository.countByProductId(product.getId());
		
		if (stockMovements > 0) {
			throw new InvalidOperationException("It was not possible to delete this product, as there are stock movements linked to this product");
		}
		
		productRepository.delete(product);
	}
}
