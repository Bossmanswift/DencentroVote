package com.metrostate.edu.decentrovote.models.security;

public class AuthToken {
    private final String token;

    public AuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}