package br.com.smartconsulting.ordermanager.core.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.smartconsulting.ordermanager.core.common.exceptions.InvalidOperationException;
import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.order.repository.OrderRepository;
import br.com.smartconsulting.ordermanager.core.stock.StockMovementRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
	
	private  ProductService service;

	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private StockMovementRepository stockMovementRepository;
	
	@Captor
	private ArgumentCaptor<ProductEntity> captor;

	@BeforeEach
	public void init() {
		this.service = new ProductServiceImpl(
			productRepository, 
			orderRepository, 
			stockMovementRepository
		);
	}
	
	@Test
	public void whenTryingToGetAProductThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(productRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.findById(id);
		});
	}
	
	@Test
	public void whenTheProductExistsTheSystemShouldReturnTheProduct() {
		
		ProductEntity product = createEntity(1l, "Coca cola 2L");
			
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

		ProductEntity response = service.findById(product.getId());

		verify(productRepository).findById(product.getId());

		assertEquals(product, response);
	}
	
	@Test
	public void whenRunningTheListItShouldReturnAllProducts() {
		ProductEntity cocaCola = createEntity(1l, "Coca cola 2L");
		ProductEntity fanta = createEntity(2l, "Fanta uva 2L");
		
		List<ProductEntity> products = new ArrayList<>();
		products.add(cocaCola);
		products.add(fanta);
		
		when(productRepository.findAll()).thenReturn(products);
		
		List<ProductEntity> response = service.findAll();
		verify(productRepository).findAll();
		
		assertEquals(products, response);
	}
	
	@Test
	public void whenTheProductIsValidTheSystemShouldSaveIt() {
		ProductEntity product = createEntity(1l, "Coca cola 2L");
		
		service.save(product);

		verify(productRepository).save(captor.capture());
		
		assertEquals(captor.getValue(), product);
	}
	
	@Test
	public void whenTryingToDeleteAProductThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(productRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.delete(id);
		});
	}
	
	@Test
	public void whenTryingToDeleteAProductThatHasOrdersTheSystemShouldThrowAnException() {
		ProductEntity product = createEntity(2l, "Coca cola 2L");

		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		when(orderRepository.countByProductId(product.getId())).thenReturn(2l);

		Exception exception = assertThrows(InvalidOperationException.class, () -> {
			service.delete(product.getId());
		});
		
		assertEquals("It was not possible to delete this product, as there are orders linked to this product", exception.getMessage());
	}
	
	@Test
	public void whenTryingToDeleteAProductThatHasStockMovementsTheSystemShouldThrowAnException() {
		ProductEntity product = createEntity(3l, "Coca cola 1L");

		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		when(orderRepository.countByProductId(product.getId())).thenReturn(0l);
		when(stockMovementRepository.countByProductId(product.getId())).thenReturn(3l);

		Exception exception = assertThrows(InvalidOperationException.class, () -> {
			service.delete(product.getId());
		});
		
		assertEquals("It was not possible to delete this product, as there are stock movements linked to this product", exception.getMessage());
	}
	
	@Test
	public void whenTheProductExistsTheSystemShouldDeleteIt() {
		ProductEntity product = createEntity(1l, "Coca cola 350ml");
		
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		
		service.delete(product.getId());

		verify(productRepository).delete(captor.capture());
		
		assertEquals(captor.getValue(), product);
	}

	private ProductEntity createEntity(long id, String name) {
		ProductEntity product = new  ProductEntity();
		product.setId(id);
		product.setName(name);
		return product;
	}
}
