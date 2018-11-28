package com.appsdeveloperblog.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;


// this is used to call database query
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{

	UserEntity findByEmail(String email);
}
