package com.metrostate.edu.decentrovote.services.interfaces;

import com.metrostate.edu.decentrovote.models.security.RoleEntity;

public interface IRoleService {
    RoleEntity findByName(String name);
}
