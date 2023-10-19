package br.com.smartconsulting.ordermanager.core.user;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface UserService {
	
	@Transactional
    public UserEntity findById(Long id);
    
    @Transactional
    public List<UserEntity> findAll();
    
    @Transactional
    public void save(UserEntity user);
    
    @Transactional
    public void delete(Long id);
}
