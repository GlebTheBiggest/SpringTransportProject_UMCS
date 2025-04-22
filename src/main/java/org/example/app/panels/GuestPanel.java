package org.example.app.panels;

import org.example.controllers.auth.Authentication;
import org.example.impls.services.input.InputService;
import org.example.models.User;

public class GuestPanel {
    private final Authentication auth;

    public GuestPanel(Authentication auth) {
        this.auth = auth;
    }

    public User display() {
        while (true) {
            char operator = InputService.getOperatorInput("""
                    GUEST MENU
                    'q' - exit the program
                    'r' - register
                    'l' - login
                    Enter your operator:\s""", new char[]{'q', 'r', 'l'});

            if (operator == 'q') return null;
            User user = (operator == 'r') ? auth.register() : auth.login();
            if (user != null) return user;
        }
    }
}
