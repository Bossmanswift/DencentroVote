package com.metrostate.edu.decentrovote.repositories;

import com.metrostate.edu.decentrovote.models.security.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
