package br.com.smartconsulting.ordermanager.api.product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.smartconsulting.ordermanager.core.product.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Products", description = "Set of APIs that allows you to create, delete and get products")
@RequestMapping(value = "/v1/products")
public class ProductController {
	
	private ProductService service;
	private ProductMapper mapper;
	
	@Autowired
	public ProductController(ProductService service, ProductMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@Valid @RequestBody ProductWritingDTO dto) {
		service.save(mapper.toEntity(dto));
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id") Long id, @Valid @RequestBody ProductWritingDTO dto) {
		service.save(mapper.toEntity(id, dto));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

	@GetMapping
	public List<ProductReadingDTO> findAll() {
		return mapper.toDTO(service.findAll());
	}

	@GetMapping("/{id}")
	public ProductReadingDTO findById(@PathVariable("id") Long id) {
		return mapper.toDTO(service.findById(id));
	}
}
