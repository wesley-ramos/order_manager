package br.com.smartconsulting.ordermanager.core.user;

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

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	
	private  UserService service;

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Captor
	private ArgumentCaptor<UserEntity> captor;

	@BeforeEach
	public void init() {
		this.service = new UserServiceImpl(userRepository, orderRepository);
	}
	
	@Test
	public void whenTryingToGetAUserThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(userRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.findById(id);
		});
	}
	
	@Test
	public void whenTheUserExistsTheSystemShouldReturnTheUser() {
		
		UserEntity user = createUserEntity(1l, "Wesley Ramos", "wesley.ramos@gmail.com");
			
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		UserEntity response = service.findById(user.getId());

		verify(userRepository).findById(user.getId());

		assertEquals(user, response);
	}
	
	@Test
	public void whenRunningTheListItShouldReturnAllUsers() {
		UserEntity wesley = createUserEntity(1l, "Wesley Ramos", "wesley.ramos@gmail.com");
		UserEntity werik = createUserEntity(2l, "Werik Ramos", "werik.ramos@gmail.com");
		
		List<UserEntity> users = new ArrayList<>();
		users.add(wesley);
		users.add(werik);
		
		when(userRepository.findAll()).thenReturn(users);
		
		List<UserEntity> response = service.findAll();
		verify(userRepository).findAll();
		
		assertEquals(users, response);
	}
	
	@Test
	public void whenTheUserIsValidTheSystemShouldSaveIt() {
		UserEntity user = createUserEntity(1l, "Wesley Ramos", "wesley.ramos@gmail.com");
		
		service.save(user);

		verify(userRepository).save(captor.capture());
		
		assertEquals(captor.getValue(), user);
	}
	
	@Test
	public void whenTryingToDeleteAUserThatDoesNotExistTheSystemShouldThrowAnException() {
		Long id = 1l;

		when(userRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.delete(id);
		});
	}
	
	@Test
	public void whenTryingToDeleteAUserThatHasOrdersTheSystemShouldThrowAnException() {
		UserEntity user = createUserEntity(1l, "Wesley Ramos", "wesley.ramos@gmail.com");

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(orderRepository.countByUserId(user.getId())).thenReturn(2l);

		assertThrows(InvalidOperationException.class, () -> {
			service.delete(user.getId());
		});
	}
	
	@Test
	public void whenTheUserExistsTheSystemShouldDeleteIt() {
		UserEntity user = createUserEntity(1l, "Wesley Ramos", "wesley.ramos@gmail.com");
		
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		
		service.delete(user.getId());

		verify(userRepository).delete(captor.capture());
		
		assertEquals(captor.getValue(), user);
	}

	private UserEntity createUserEntity(long id, String name, String email) {
		UserEntity user = new  UserEntity();
		user.setId(id);
		user.setName(name);
		user.setEmail(email);
		return user;
	}
}
