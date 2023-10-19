package br.com.smartconsulting.ordermanager.core.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	public List<UserEntity> findAll();
}
