package org.example.app.panels.admin.panels;

import org.example.controllers.auth.Authentication;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.models.Rental;
import org.example.models.User;

import java.util.List;

import static org.example.impls.services.InputService.*;

public class AdminUserPanel {
    private User USER;
    private final UserRepoService userService;
    private final RentalRepoService rentalService;
    private final Authentication auth;

    public AdminUserPanel(User user, UserRepoService userService, RentalRepoService rentalService, Authentication auth) {
        this.USER = user;
        this.userService = userService;
        this.rentalService = rentalService;
        this.auth = auth;
    }

    public boolean start() {
        while (true) {
            System.out.println("USER MANAGEMENT MENU");
            System.out.println("""
                    1 - Get all users
                    2 - Get user by ID
                    3 - Add user
                    4 - Remove user
                    b - Step back
                    q - Log out""");
            char operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', 'b', 'q'});
            switch (operator) {
                case '1' -> fetchAllUsers();
                case '2' -> getUserById();
                case '3' -> addUser();
                case '4' -> {
                    if (!removeUser()) {
                        return false;
                    }
                }
                case 'b' -> {
                    return true;
                }
                case 'q' -> {
                    System.out.println("Logging out...");
                    return false;
                }
            }
        }
    }

    private void fetchAllUsers() {
        try {
            userService.printAllUsers();
        } catch (Exception e) {
            System.out.println("An error occurred while fetching users: " + e.getMessage());
        }
    }

    private void getUserById() {
        String userId = getUserInput("Enter the ID of the user you want to view: ");
        if (userId.isBlank()) {
            System.out.println("Invalid or empty user ID!");
            return;
        }
        try {
            User user = userService.getUserRepo().getUserById(userId);
            if (user == null) {
                System.out.println("User with ID " + userId + " does not exist!");
            } else {
                System.out.println(user);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching the user: " + e.getMessage());
        }
    }

    private void addUser() {
        try {
            if (auth.register() != null) {
                System.out.println("User has been added successfully!");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while adding the user: " + e.getMessage());
        }
    }

    private boolean removeUser() {
        String userId = getUserInput("Enter the ID of the user you want to remove: ");
        if (userId == null || userId.isBlank()) {
            System.out.println("Invalid or empty user ID!");
            return true;
        }
        try {
            User user = fetchUser(userId);
            if (user == null) {
                return true;
            }

            List<Rental> rentals = fetchRentalsByUser(userId);
            if (!rentals.isEmpty() && !confirmAction("This user is renting at the moment. Are you sure (yes/no)? ")) {
                System.out.println("The operation was aborted!");
                return true;
            }

            if (USER.getId().equals(userId)) {
                if (!confirmAction("You are trying to remove yourself. Are you sure (yes/no)? ")) {
                    System.out.println("The operation was aborted!");
                    return true;
                }
                USER = null;
                removeRentals(rentals);
                userService.getUserRepo().remove(userId);
                userService.getUserRepo().saveCsv();
                System.out.println("Your account has been removed successfully!");
                return false;
            } else {
                removeRentals(rentals);
                userService.getUserRepo().remove(userId);
                userService.getUserRepo().saveCsv();
                System.out.println("User has been removed successfully!");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while removing the user: " + e.getMessage());
        }
        return true;
    }

    private User fetchUser(String userId) {
        try {
            User user = userService.getUserRepo().getUserById(userId);
            if (user == null) {
                System.out.println("User with ID " + userId + " does not exist!");
                return null;
            }
            return user;
        } catch (Exception e) {
            System.out.println("An error occurred while fetching the user: " + e.getMessage());
            return null;
        }
    }

    private List<Rental> fetchRentalsByUser(String userId) {
        try {
            return rentalService.getRentalRepo().getByUserId(userId);
        } catch (Exception e) {
            System.out.println("An error occurred while fetching rentals: " + e.getMessage());
            return List.of();
        }
    }

    private void removeRentals(List<Rental> rentals) {
        try {
            for (Rental rental : rentals) {
                rentalService.getRentalRepo().remove(rental.getId());
            }
            rentalService.getRentalRepo().saveCsv();
        } catch (Exception e) {
            System.out.println("An error occurred while removing rentals: " + e.getMessage());
        }
    }
}