package com.metrostate.edu.decentrovote.models.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.metrostate.edu.decentrovote.models.security.interfaces.VoterUserJsonView;

public class UserDTO {
    private String username;
    private String password;
    private String name;
    private boolean isSystemAdmin;
    private boolean isElectionAdmin;

    @JsonIgnore
    public UserEntity getUserFromDTO(){
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        return user;
    }

    @JsonView(VoterUserJsonView.VoterFields.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(VoterUserJsonView.VoterFields.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(VoterUserJsonView.VoterFields.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonView(VoterUserJsonView.AllFields.class)
    public boolean isSystemAdmin() {
        return isSystemAdmin;
    }

    public void setSystemAdmin(boolean systemAdmin) {
        isSystemAdmin = systemAdmin;
    }

    @JsonView(VoterUserJsonView.AllFields.class)
    public boolean isElectionAdmin() {
        return isElectionAdmin;
    }

    public void setElectionAdmin(boolean electionAdmin) {
        isElectionAdmin = electionAdmin;
    }
}
