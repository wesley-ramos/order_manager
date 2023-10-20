package br.com.smartconsulting.ordermanager.api.order;

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

import br.com.smartconsulting.ordermanager.core.order.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Orders", description = "Module for order management")
@RequestMapping(value = "/v1/orders")
public class OrderController {
	
	private OrderService service;
	private OrderMapper mapper;
	
	@Autowired
	public OrderController(OrderService service, OrderMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@Valid @RequestBody OrderWritingDTO dto) {
		service.save(mapper.toEntity(dto));
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id") Long id, @Valid @RequestBody OrderWritingDTO dto) {
		service.save(mapper.toEntity(id, dto));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

	@GetMapping
	public List<OrderReadingDTO> findAll() {
		return mapper.toDTO(service.findAll());
	}

	@GetMapping("/{id}")
	public OrderReadingDTO findById(@PathVariable("id") Long id) {
		return mapper.toDTO(service.findById(id));
	}
}
