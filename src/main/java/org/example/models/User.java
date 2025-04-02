package org.example.models;

import lombok.Data;

@Data
public class User {
    private int id;
    private int vehicleId;
    private String login;
    private String hashPassword;
    private String role;

    public User(int id, int vehicleId, String login, String hashPassword, String role) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.login = login;
        this.hashPassword = hashPassword;
        this.role = role;
    }

    public User(String login, String hashPassword, String role) {
        this(0, 0, login, hashPassword, role);
    }

    public String toCsv() {
        return id + "," + vehicleId + "," + login + "," + hashPassword + "," + role; // Використовуємо кому
    }

    public User cloneUser() {
        return new User(id, vehicleId, login, hashPassword, role);
    }
}
