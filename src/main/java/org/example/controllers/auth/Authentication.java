package org.example.controllers.auth;

import org.example.interfaces.services.UserRepoService;
import org.example.models.User;

import java.util.Scanner;

import static org.example.security.PasswordHasher.hashPassword;

public class Authentication {
    private final Scanner scanner;
    private final UserRepoService userService;
    private User currentUser;

    public Authentication(Scanner scanner, UserRepoService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    public User login() {
        String login = getUserInput("Enter your login: ");
        String password = getUserInput("Enter your password: ");

        for (User user : userService.getUserRepo().getAll()) {
            if (login.equals(user.getLogin()) && hashPassword(password).equals(user.getHashPassword())) {
                System.out.println("Login successful!");
                currentUser = user;
                return user;
            }
        }
        System.out.println("Invalid login or password! Please try again.");
        return null;
    }

    public User register() {
        String login = getUserInput("Enter your login: ");
        if (userService.getUserRepo().getAll().stream().anyMatch(u -> u.getLogin().equals(login))) {
            System.out.println("This login is already taken. Please try again.");
            return null;
        }
        String password = getUserInput("Enter your password: ");
        String role = getUserInput("Enter your role (admin/user): ").toLowerCase();

        User newUser = new User(login, hashPassword(password), role);
        userService.getUserRepo().add(newUser);
        currentUser = newUser;
        return newUser;
    }

    private String getUserInput(String message) {
        System.out.print(message);
        return scanner.next();
    }
}
