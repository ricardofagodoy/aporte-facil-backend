package com.aportefacil.backend.model;

public class User {

    private final String firstName;
    private final String token;

    public User(String token, String firstName) {
        this.token = token;
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getToken() {
        return token;
    }
}