package com.metrostate.edu.decentrovote.services;

import com.metrostate.edu.decentrovote.models.security.RoleEntity;
import com.metrostate.edu.decentrovote.repositories.RoleRepository;
import com.metrostate.edu.decentrovote.services.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "roleService")
public class RoleServiceImpl extends AbstractDataAccessService<RoleEntity> implements IRoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleEntity findByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public Class<RoleEntity> getEntityClass() {
        return RoleEntity.class;
    }
}
