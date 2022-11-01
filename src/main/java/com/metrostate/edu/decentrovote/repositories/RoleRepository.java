package com.metrostate.edu.decentrovote.repositories;

import com.metrostate.edu.decentrovote.models.security.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findRoleByName(String name);
}
