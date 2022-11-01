package com.metrostate.edu.decentrovote.controllers;

import com.metrostate.edu.decentrovote.models.security.AuthToken;
import com.metrostate.edu.decentrovote.models.security.LoginUser;
import com.metrostate.edu.decentrovote.models.security.UserEntity;
import com.metrostate.edu.decentrovote.models.security.UserDTO;
import com.metrostate.edu.decentrovote.security.TokenProvider;
import com.metrostate.edu.decentrovote.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.metrostate.edu.decentrovote.security.WebSecurityConfig.SECURITY_CONFIG_NAME;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@Tag(name = "User Operations REST API", description = "Use this API to Authenticate and Register a User")
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider jwtTokenUtil;

    private final IUserService userService;

    public UserController(AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil, IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@org.springframework.web.bind.annotation.RequestBody LoginUser loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public UserEntity saveUser(@RequestBody UserDTO user){
        return userService.save(user);
    }

    @RequestMapping(value="/all-users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @SecurityRequirement(name = SECURITY_CONFIG_NAME)
    public List<UserEntity> getAllUsers() {
        return userService.findAll();
    }

    @RequestMapping(value="/grant-role/{username}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @SecurityRequirement(name = SECURITY_CONFIG_NAME)
    public UserEntity grantRoleToUserByUsername (@PathVariable String username,
                                                 @RequestParam Boolean electionAdmin,
                                                 @RequestParam Boolean systemAdmin) {
        return userService.grantRole(username, electionAdmin, systemAdmin);
    }

    @RequestMapping(value="/revoke-role/{username}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @SecurityRequirement(name = SECURITY_CONFIG_NAME)
    public UserEntity revokeUserRoleByUsername (@PathVariable String username,
                                                @RequestParam Boolean electionAdmin,
                                                @RequestParam Boolean systemAdmin) {
        return userService.revokeRole(username, electionAdmin, systemAdmin);
    }

    @RequestMapping(value="/delete/{username}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @SecurityRequirement(name = SECURITY_CONFIG_NAME)
    public void deleteByUserUsername (@PathVariable String username) {
        userService.deleteUserByUserName(username);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value="/userping", method = RequestMethod.GET)
    public String userPing() {
        return "Anybody Authenticated Can Read This";
    }
}
