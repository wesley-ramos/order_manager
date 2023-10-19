package br.com.smartconsulting.ordermanager.core.user;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository repository;
	
	@Autowired
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserEntity findById(Long id) {
		return repository.findById(id)
            .orElseThrow(() -> new NotFoundException(format("User %d was not found", id)));
	}

	@Override
	public List<UserEntity> findAll() {
		return repository.findAll();
	}

	@Override
	public void save(UserEntity user) {
		repository.save(user);
	}
	
	//TODO Do not allow to delete when there is an order
	@Override
	public void delete(Long id) {
		UserEntity user = this.findById(id);
		repository.delete(user);
	}
}
