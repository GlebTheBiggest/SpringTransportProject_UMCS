package org.example.controllers;

import org.example.controllers.auth.Authentication;
import org.example.impls.repositories.UserRepoImpl;
import org.example.impls.repositories.VehicleRepoImpl;
import org.example.impls.services.UserRepoServiceImpl;
import org.example.impls.services.VehicleRepoServiceImpl;
import org.example.interfaces.repositories.UserRepo;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.Car;
import org.example.models.Motorcycle;
import org.example.models.User;
import org.example.models.abstractions.Vehicle;

import java.util.Scanner;

public class ConsoleController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepo userRepo = new UserRepoImpl();
    private static final VehicleRepo vehicleRepo = new VehicleRepoImpl();
    private static final UserRepoService userService = new UserRepoServiceImpl(userRepo);
    private static final VehicleRepoService vehicleService = new VehicleRepoServiceImpl(vehicleRepo);
    private static final Authentication auth = new Authentication(scanner, userService);
    private static User USER;

    public static void console() {
        System.out.println("Hello World!");
        char operator = 'd';
        while (true) {
            if (operator == 'd') {
                operator = getOperatorInput("'q' - exit the program\n" +
                                            "'r' - register\n" +
                                            "'l' - login\n" +
                                            "Enter your operator: ", new char[]{'q', 'r', 'l'});
            }
            if (operator == 'q') break;

            USER = (operator == 'r') ? auth.register() : auth.login();

            if (USER != null) {
                if (USER.getRole().equalsIgnoreCase("admin")) adminAction();
                else userAction();
            } else {
                operator = 'd';
            }

        }
        System.out.println("Exiting...");
    }

    private static void userAction() {
        char operator;
        scanner.nextLine();
        while (true) {
            System.out.println("\nUSER MENU");
            System.out.println("""
                    1 - Rent vehicle
                    2 - Return vehicle
                    3 - Your account
                    q - Log out""");
            operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', 'q'});
            switch (operator) {
                case '1' -> rentVehicle();
                case '2' -> returnVehicle();
                case '3' -> System.out.println(USER);
                case 'q' -> {
                    System.out.println("Logging out...");
                    return;
                }
            }
        }
    }

    private static void adminAction() {
        char operator = ' ';
        scanner.nextLine();
        while (operator != 'q') {
            System.out.println("ADMIN MENU");
            System.out.println("""
                    1 - Get all users
                    2 - Get user by ID
                    3 - Add user
                    4 - Remove user
                    5 - Get all vehicles
                    6 - Get vehicle by ID
                    7 - Add vehicle
                    8 - Remove Vehicle
                    9 - Your account
                    q - Log out""");
            operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'q'});
            switch (operator) {
                case '1' -> userService.printAllUsers();
                case '2' -> getUserById();
                case '3' -> {
                    if (auth.register() != null) System.out.println("User has been added successfully!");
                }
                case '4' -> {
                    if (!removeUser()) {
                        operator = 'q';
                    }
                }
                case '5' -> vehicleService.printAllVehicles();
                case '6' -> getVehicleById();
                case '7' -> addVehicle();
                case '8' -> removeVehicle();
                case '9' -> System.out.println(USER);
                case 'q' -> {
                    System.out.println("Logging out...");
                    return;
                }
            }
        }
    }

    private static void rentVehicle() {
        if (USER.getVehicleId() != 0) {
            System.out.println("You have already rented a vehicle (ID: " + USER.getVehicleId() + "). Return it first.");
            return;
        }

        int vehicleId = Integer.parseInt(getUserInput("Enter the ID of the vehicle you want to rent: "));
        Vehicle vehicle = vehicleService.getVehicleRepo().getVehicleById(vehicleId);

        if (vehicle == null) {
            System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            return;
        }

        if (vehicle.isRented()) {
            System.out.println("This vehicle is already rented! Try another one.");
            return;
        }

        vehicleService.rentVehicle(vehicleId);
        userService.getUserRepo().getUserById(USER.getId()).setVehicleId(vehicleId);
        userService.getUserRepo().save();
        USER.setVehicleId(vehicleId);
        System.out.println("Vehicle (ID: " + vehicleId + ") rented successfully!");
    }

    private static void returnVehicle() {
        if (USER.getVehicleId() == 0) {
            System.out.println("You do not have any rented vehicle!");
            return;
        }

        int vehicleId = Integer.parseInt(getUserInput("Enter the ID of the vehicle you want to return: "));

        if (USER.getVehicleId() != vehicleId) {
            System.out.println("You did not rent this vehicle! Please enter the correct ID.");
            return;
        }

        Vehicle vehicle = vehicleService.getVehicleRepo().getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            return;
        }

        vehicleService.returnVehicle(vehicleId);
        userService.getUserRepo().getUserById(USER.getId()).setVehicleId(0);
        userService.getUserRepo().save();
        USER.setVehicleId(0);
        System.out.println("Vehicle (ID: " + vehicleId + ") returned successfully!");
    }

    private static void getUserById() {
        int userId = Integer.parseInt(getUserInput("Enter the ID of the user you want to view: "));
        User user = userService.getUserRepo().getUserById(userId);
        if (user == null) {
            System.out.println("User with ID " + userId + " does not exist!");
            return;
        }
        System.out.println(user);
    }

    private static void getVehicleById() {
        int vehicleId = Integer.parseInt(getUserInput("Enter the ID of the vehicle you want to view: "));
        Vehicle vehicle = vehicleService.getVehicleRepo().getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            return;
        }
        System.out.println(vehicle);
    }

    private static boolean removeUser() {
        int userId = Integer.parseInt(getUserInput("Enter the ID of the user you want to remove: "));
        User user = userService.getUserRepo().getUserById(userId);

        if (user == null) {
            System.out.println("User with ID " + userId + " does not exist!");
            return true;
        }

        if (USER.getId() == userId) {
            System.out.println("You are trying to remove yourself. Are you sure (yes/no)? ");
            while (true) {
                String userInput = getUserInput("Enter your choice: ");
                if (userInput.equalsIgnoreCase("yes")) {
                    userService.getUserRepo().delete(userId);
                    System.out.println("Your account has been removed successfully!");
                    return false;
                } else if (userInput.equalsIgnoreCase("no")) {
                    return true;
                } else {
                    System.out.println("Invalid choice! Try again.");
                }
            }
        } else {
            System.out.println("User has been removed successfully!");
            userService.getUserRepo().delete(userId);
            return true;
        }
    }

    private static void addVehicle() {
        char type = getOperatorInput("'m' for motorcycle\n'c' for car\n Enter your choice: ", new char[]{'m', 'c'});
        String brand = getUserInput("Enter the brand: ");
        String model = getUserInput("Enter the model: ");
        int year = Integer.parseInt(getUserInput("Enter the year: "));
        int price = Integer.parseInt(getUserInput("Enter the price: "));
        boolean isRented = false;

        if (type == 'c') {
            vehicleService.getVehicleRepo().add(new Car(brand, model, year, price, isRented));
        } else {
            String category = getUserInput("Enter the category: ");
            vehicleService.getVehicleRepo().add(new Motorcycle(brand, model, year, price, isRented, category));
        }
        System.out.println("Vehicle added successfully!");
    }

    private static void removeVehicle() {
        int vehicleId = Integer.parseInt(getUserInput("Enter the ID of the vehicle you want to remove: "));
        Vehicle vehicle = vehicleService.getVehicleRepo().getVehicleById(vehicleId);

        if (vehicle == null) {
            System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            return;
        }

        vehicleService.getVehicleRepo().delete(vehicleId);
        System.out.println("Vehicle removed successfully!");
    }

    private static char getOperatorInput(String message, char[] validOptions) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine().trim();
            if (input.length() == 1) {
                char option = input.charAt(0);
                for (char valid : validOptions) {
                    if (option == valid) return option;
                }
            }
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static String getUserInput(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

}
