package com.metrostate.edu.decentrovote.services;

import com.metrostate.edu.decentrovote.models.security.RoleEntity;
import com.metrostate.edu.decentrovote.models.security.TwelveWordMnemonicSentence;
import com.metrostate.edu.decentrovote.models.security.UserDTO;
import com.metrostate.edu.decentrovote.models.security.UserEntity;
import com.metrostate.edu.decentrovote.repositories.UserRepository;
import com.metrostate.edu.decentrovote.services.interfaces.IRoleService;
import com.metrostate.edu.decentrovote.services.interfaces.IUserService;
import com.metrostate.edu.decentrovote.utils.crypto.MnemonicCodeGeneratorUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.metrostate.edu.decentrovote.utils.crypto.MnemonicCodeGeneratorUtil.generateMnemonicCodeWordList;

@Service(value = "userService")
public class UserServiceImpl extends AbstractDataAccessService<UserEntity> implements UserDetailsService, IUserService {

    private final IRoleService roleService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bcryptEncoder;

    public UserServiceImpl(IRoleService roleService, UserRepository userRepository, BCryptPasswordEncoder bcryptEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    public List<UserEntity> findAll() {
        List<UserEntity> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity grantRole(String username, boolean electionAdmin, boolean systemAdmin) {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (electionAdmin) {
            RoleEntity roleEntity = roleService.findByName("ELECT_ADMIN");
            if (userEntity.getRoles().contains(roleEntity)) {
                throw new BadRequestException("User already has election admin role");
            }
            userEntity.addRole(roleEntity);
        }

        if (systemAdmin) {
            RoleEntity roleEntity = roleService.findByName("SYS_ADMIN");
            if (userEntity.getRoles().contains(roleEntity)) {
                throw new BadRequestException("User already has system admin role");
            }
            userEntity.addRole(roleEntity);
        }
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity revokeRole(String username, boolean electionAdmin, boolean systemAdmin) {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (electionAdmin) {
            RoleEntity roleEntity = roleService.findByName("ELECT_ADMIN");
            if (!userEntity.getRoles().contains(roleEntity)) {
                throw new BadRequestException("User does not have the election admin role");
            }
            userEntity.removeRole(roleEntity);
        }

        if (systemAdmin) {
            RoleEntity roleEntity = roleService.findByName("SYS_ADMIN");
            if (!userEntity.getRoles().contains(roleEntity)) {
                throw new BadRequestException("User does not have the system admin role");
            }
            userEntity.removeRole(roleEntity);
        }
        return userRepository.save(userEntity);
    }

    @Override
    public void deleteUserByUserName(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new BadRequestException(String.format("User with username %s does not exist", username));
        }
        userRepository.delete(userEntity);
    }

    @Override
    public UserEntity save(UserDTO userDTO) {
        UserEntity newUser = userDTO.getUserFromDTO();
        newUser.setPassword(bcryptEncoder.encode(userDTO.getPassword()));

        RoleEntity roleEntity = roleService.findByName("VOTER");
        Set<RoleEntity> roleSet = new HashSet<>();
        roleSet.add(roleEntity);

        /*roleEntity = roleService.findByName("SYS_ADMIN");
        roleSet.add(roleEntity);
        roleEntity = roleService.findByName("ELECT_ADMIN");
        roleSet.add(roleEntity);*/

        if(userDTO.isSystemAdmin()){
            roleEntity = roleService.findByName("SYS_ADMIN");
            roleSet.add(roleEntity);
        } else if (userDTO.isElectionAdmin()){
            roleEntity = roleService.findByName("ELECT_ADMIN");
            roleSet.add(roleEntity);
        }
        newUser.setRoles(roleSet);

        try {
            TwelveWordMnemonicSentence mnemonicCode = new TwelveWordMnemonicSentence(generateMnemonicCodeWordList());
            newUser.setMnemonicCode(mnemonicCode);
            newUser.setEncryptedMnemonicCode(generateEncryptedMnemonicCode(newUser));
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
        return userRepository.save(newUser);
    }

    private String generateEncryptedMnemonicCode(UserEntity newUser) {
        return MnemonicCodeGeneratorUtil.encodeMnemonicWordListWithPassPhrase(newUser.getMnemonicCode().getWordList(), newUser.getPassword()); // Todo: change to word twelve as opposed to password
    }

    @Override
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }
}
