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

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
	
	private  ProductService service;

	@Mock
	private ProductRepository repository;
	
	@Captor
	private ArgumentCaptor<ProductEntity> captor;

	@BeforeEach
	public void init() {
		this.service = new ProductServiceImpl(repository);
	}
	
	@Test
	public void whenTryingToGetAProductThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.findById(id);
		});
	}
	
	@Test
	public void whenTheProductExistsTheSystemShouldReturnTheProduct() {
		
		ProductEntity product = createEntity(1l, "Coca cola 2L");
			
		when(repository.findById(product.getId())).thenReturn(Optional.of(product));

		ProductEntity response = service.findById(product.getId());

		verify(repository).findById(product.getId());

		assertEquals(product, response);
	}
	
	@Test
	public void whenRunningTheListItShouldReturnAllProducts() {
		ProductEntity cocaCola = createEntity(1l, "Coca cola 2L");
		ProductEntity fanta = createEntity(2l, "Fanta uva 2L");
		
		List<ProductEntity> products = new ArrayList<>();
		products.add(cocaCola);
		products.add(fanta);
		
		when(repository.findAll()).thenReturn(products);
		
		List<ProductEntity> response = service.findAll();
		verify(repository).findAll();
		
		assertEquals(products, response);
	}
	
	@Test
	public void whenTheProductIsValidTheSystemShouldSaveIt() {
		ProductEntity product = createEntity(1l, "Coca cola 2L");
		
		service.save(product);

		verify(repository).save(captor.capture());
		
		assertEquals(captor.getValue(), product);
	}
	
	@Test
	public void whenTryingToDeleteAProductThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.delete(id);
		});
	}
	
	@Test
	public void whenTheProductExistsTheSystemShouldDeleteIt() {
		ProductEntity product = createEntity(1l, "Wesley Ramos");
		
		when(repository.findById(product.getId())).thenReturn(Optional.of(product));
		
		service.delete(product.getId());

		verify(repository).delete(captor.capture());
		
		assertEquals(captor.getValue(), product);
	}

	private ProductEntity createEntity(long id, String name) {
		ProductEntity product = new  ProductEntity();
		product.setId(id);
		product.setName(name);
		return product;
	}
}
