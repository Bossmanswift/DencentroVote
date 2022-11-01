package com.metrostate.edu.decentrovote.services.interfaces;

import com.metrostate.edu.decentrovote.models.security.UserEntity;
import com.metrostate.edu.decentrovote.models.security.UserDTO;

import java.util.List;

public interface IUserService {
    UserEntity save(UserDTO user);
    List<UserEntity> findAll();
    UserEntity findByUsername(String username);
    UserEntity grantRole(String username, boolean electionAdmin, boolean systemAdmin);
    UserEntity revokeRole(String username, boolean electionAdmin, boolean systemAdmin);
    void deleteUserByUserName(String username);
}
