package org.example.models;

import lombok.Data;

@Data
public class User {
    private String id;
    private String login;
    private String password;
    private String role;

    public User(String id, String login, String password, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String toCsv() {
        return id + "," + login + "," + password + "," + role;
    }

    public User cloneUser() {
        return new User(id, login, password, role);
    }
}
