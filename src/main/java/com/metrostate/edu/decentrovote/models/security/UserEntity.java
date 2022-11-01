package com.metrostate.edu.decentrovote.models.security;

import com.metrostate.edu.decentrovote.models.security.interfaces.IMnemonicSentence;
import org.hibernate.annotations.NaturalId;

import javax.management.relation.Role;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "`USER`")
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String name;
    private String password;
    @NaturalId
    private String encryptedMnemonicCode;

    @Transient
    private TwelveWordMnemonicSentence mnemonicCode;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "USERROLES",
            joinColumns = {
                    @JoinColumn(name = "userId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "roleId") })
    private Set<RoleEntity> roles = new HashSet<>();

    public UserEntity() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public void addRole (RoleEntity roleEntity) {
        this.roles.add(roleEntity);
    }

    public void removeRole (RoleEntity roleEntity) {
        this.roles.remove(roleEntity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TwelveWordMnemonicSentence getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(TwelveWordMnemonicSentence mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public String getEncryptedMnemonicCode() {
        return encryptedMnemonicCode;
    }

    public void setEncryptedMnemonicCode(String encryptedMnemonicCode) {
        this.encryptedMnemonicCode = encryptedMnemonicCode;
    }
}
