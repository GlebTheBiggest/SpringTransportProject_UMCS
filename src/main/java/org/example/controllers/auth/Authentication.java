package org.example.controllers.auth;

import org.example.interfaces.services.UserRepoService;
import org.example.models.User;

import java.util.Scanner;

import static org.example.impls.services.InputService.getUserInput;
import static org.example.security.Hasher.checkString;
import static org.example.security.IdGenerator.generateId;
import static org.example.security.Hasher.hashString;

public class Authentication {
    private final UserRepoService userService;

    public Authentication(UserRepoService userService) {
        this.userService = userService;
    }

    public User login() {
        String login = getUserInput("Enter your login: ");
        String password = getUserInput("Enter your password: ");

        for (User user : userService.getUserRepo().getAll()) {
            if (login.equals(user.getLogin()) && checkString(password, user.getPassword())) {
                System.out.println("Login successful!");
                return user;
            }
        }
        System.out.println("Invalid login or password! Please try again.");
        return null;
    }

    public User register() {
        String id = generateId();
        String login = getUserInput("Enter your login: ");
        if (userService.getUserRepo().getByLogin(login) != null) {
            System.out.println("This login is already taken. Please try again.");
            return null;
        }
        String password = getUserInput("Enter your password: ");
        String role = getUserInput("Enter your role (admin/user): ").toUpperCase();
        if (role.equals("ADMIN")) {
            String adminPassword = getUserInput("Enter admin password: ");
            if (adminPassword.equals("admin")) {
                User newUser = new User(id, login, hashString(password), role);
                userService.getUserRepo().add(newUser);
            } else {
                System.out.println("Invalid password. Please try again.");
                return null;
            }
        }
        User newUser = new User(id, login, hashString(password), role);
        userService.getUserRepo().add(newUser);
        return newUser;
    }
}
