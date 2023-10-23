package br.com.smartconsulting.ordermanager.api.user;

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

import br.com.smartconsulting.ordermanager.core.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Users", description = "Set of APIs that allows you to create, delete and get users")
@RequestMapping(value = "/v1/users")
public class UserController {
	
	private UserService service;
	private UserMapper mapper;
	
	@Autowired
	public UserController(UserService service, UserMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@Valid @RequestBody UserWritingDTO dto) {
		service.save(mapper.toEntity(dto));
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id") Long id, @Valid @RequestBody UserWritingDTO dto) {
		service.save(mapper.toEntity(id, dto));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

	@GetMapping
	public List<UserReadingDTO> findAll() {
		return mapper.toDTO(service.findAll());
	}

	@GetMapping("/{id}")
	public UserReadingDTO findById(@PathVariable("id") Long id) {
		return mapper.toDTO(service.findById(id));
	}
}
