package org.example.models;

import java.util.Objects;

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
        this(0, -1, login, hashPassword, role);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String toCsv() {
        return id + "," + vehicleId + "," + login + "," + hashPassword + "," + role; // Використовуємо кому
    }

    public User cloneUser() {
        return new User(id, vehicleId, login, hashPassword, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && vehicleId == user.vehicleId && Objects.equals(login, user.login) && Objects.equals(hashPassword, user.hashPassword) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vehicleId, login, hashPassword, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", vehicleId=" + vehicleId +
                ", name='" + login + '\'' +
                ", hashPassword=" + hashPassword +
                ", role='" + role + '\'' +
                "}";
    }
}
