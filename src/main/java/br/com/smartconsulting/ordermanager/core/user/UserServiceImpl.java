package br.com.smartconsulting.ordermanager.core.user;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.InvalidOperationException;
import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;
import br.com.smartconsulting.ordermanager.core.order.repository.OrderRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private OrderRepository orderRepository;

	@Autowired
	public UserServiceImpl(
			UserRepository userRepository, 
			OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	@Override
	public UserEntity findById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(format("User %d was not found", id)));
	}

	@Override
	public List<UserEntity> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void save(UserEntity user) {
		userRepository.save(user);
	}

	@Override
	public void delete(Long id) {
		UserEntity user = this.findById(id);

		Long orders = orderRepository.countByUserId(user.getId());

		if (orders > 0) {
			throw new InvalidOperationException("It was not possible to delete this user, as there are orders linked to this user");
		}

		userRepository.delete(user);
	}
}
