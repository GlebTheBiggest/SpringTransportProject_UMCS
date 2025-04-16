package org.example.app;

import org.example.controllers.auth.Authentication;
import org.example.impls.repositories.RentalRepoImpl;
import org.example.impls.repositories.UserRepoImpl;
import org.example.impls.repositories.VehicleRepoImpl;
import org.example.impls.services.RentalRepoServiceImpl;
import org.example.impls.services.UserRepoServiceImpl;
import org.example.impls.services.VehicleRepoServiceImpl;
import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.repositories.UserRepo;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;

import java.util.*;

import static org.example.security.IdGenerator.generateId;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepo userRepo = new UserRepoImpl();
    private static final VehicleRepo vehicleRepo = new VehicleRepoImpl();
    private static final UserRepoService userService = new UserRepoServiceImpl(userRepo);
    private static final VehicleRepoService vehicleService = new VehicleRepoServiceImpl(vehicleRepo);
    private static final RentalRepo rentalRepo = new RentalRepoImpl();
    private static final RentalRepoService rentalService = new RentalRepoServiceImpl(rentalRepo);
    private static final Authentication auth = new Authentication(scanner, userService);
    private static User USER;

    public static void run() {
        System.out.println("Hello World!");
        char operator;
        while (true) {
            operator = getOperatorInput("""
                    Guest MENU
                    'q' - exit the program
                    'r' - register
                    'l' - login
                    Enter your operator:\s""", new char[]{'q', 'r', 'l'});
            if (operator == 'q') break;
            USER = (operator == 'r') ? auth.register() : auth.login();
            if (USER != null) {
                if (USER.getRole().equalsIgnoreCase("ADMIN")) adminAction();
                else userAction();
            }
            scanner.nextLine();
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
            System.out.println("1 - User Management\n2 - Vehicle Management\n3 - Rental Management\n4 - Your Account\nq - Log out");
            //System.out.println(" 1 - Get all users\n2 - Get user by ID 3 - Add user 4 - Remove user 5 - Get all vehicles 6 - Get vehicle by ID 7 - Add vehicle 8 - Remove Vehicle 9 - Get all rentals 10 - Get rental by ID 11 - Add rental 12 - Remove rental 9 - Your account q - Log out");
            operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', 'q'});
            switch (operator) {
                case '1' -> {
                    if (!userManagementAction()) {
                        return;
                    }
                }
                case '2' -> {
                    if (!vehicleManagementAction()) {
                        return;
                    }
                }
                case '3' -> {
                    if (!rentalManagementAction()) {
                        return;
                    }
                }
            }
        }
    }

    private static boolean userManagementAction() {
        char operator;
        scanner.nextLine();
        while (true) {
            System.out.println("USER MANAGEMENT MENU");
            System.out.println("""
                    1 - Get all users
                    2 - Get user by ID
                    3 - Add user
                    4 - Remove user
                    b - Step back
                    q - Log out""");
            operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', 'b', 'q'});
            switch (operator) {
                case '1' -> userService.printAllUsers();
                case '2' -> getUserById();
                case '3' -> {
                    if (auth.register() != null) System.out.println("User has been added successfully!");
                }
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

    private static boolean vehicleManagementAction() {
        char operator;
        scanner.nextLine();
        while (true) {
            System.out.println("VEHICLE MANAGEMENT MENU");
            System.out.println("""
                    1 - Get all vehicles
                    2 - Get vehicle by ID
                    3 - Add vehicle
                    4 - Remove vehicle
                    b - Step back
                    q - Log out""");
            operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', 'b', 'q'});
            switch (operator) {
                case '1' -> vehicleService.printAllVehicles();
                case '2' -> getVehicleById();
                case '3' -> addVehicle();
                case '4' -> removeVehicle();
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

    private static boolean rentalManagementAction() {
        char operator;
        scanner.nextLine();
        while (true) {
            System.out.println("RENTAL MANAGEMENT MENU");
            System.out.println("""
                    1 - Get all rentals
                    2 - Get rental by ID
                    3 - Add rental
                    4 - Remove rental
                    b - Step back
                    q - Log out""");
            operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', 'b', 'q'});
            switch (operator) {
                case '1' -> rentalService.printAllRentals();
                case '2' -> getRentalById();
                case '3' -> addRental();
                case '4' -> removeRental();
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

    private static void rentVehicle() {
        List<Vehicle> availableVehicles = vehicleService.getAllAvailableVehicles(rentalService.getRentalRepo());
        if (availableVehicles.isEmpty()) {
            System.out.println("There are no vehicles available!");
            return;
        }
        System.out.println("Available vehicles: \n");
        for (int i = 0; i < availableVehicles.size(); i++) {
            System.out.println((i + 1) + ": " + availableVehicles.get(i));
        }
        String operator;
        operator = getUserInput("Enter the number of the vehicle you want to rent or b for step back: ");
        while (!operator.equals("b")) {
            int vehicleNumber = Integer.parseInt(operator);
            if (vehicleNumber - 1 < availableVehicles.size() && vehicleNumber - 1 >= 0) {
                String rentDate = getUserInput("Enter the date of the rent: ");
                String expirationDate = getUserInput("Enter the expiration date of the rent: ");
                rentalService.rentVehicle(availableVehicles.get(vehicleNumber - 1).getId(), USER.getId(), rentDate, expirationDate);
                rentalService.getRentalRepo().save();
                System.out.println("Vehicle (ID: " + availableVehicles.get(vehicleNumber - 1).getId() + ") rented successfully!");
            } else {
                System.out.println("Invalid selection. Please try again.");
                operator = getUserInput("Enter the number of the vehicle you want to rent or b for step back: ");
            }
        }
    }

    private static void returnVehicle() {
        List<Rental> userRentals = new ArrayList<>();
        for (Rental r : rentalService.getRentalRepo().getAll()) {
            if (USER.getId().equals(r.getUserId())) {
                userRentals.add(r);
            }
        }
        if (userRentals.isEmpty()) {
            System.out.println("You have no rented vehicles to return!");
            return;
        }
        System.out.println("Your rented vehicles:\n");
        for (int i = 0; i < userRentals.size(); i++) {
            System.out.println((i + 1) + ": " + userRentals.get(i));
        }
        String operator;
        operator = getUserInput("Enter the number of the vehicle you want to return or b for step back: ");
        while (!operator.equals("b")) {
            try {
                int rentalNumber = Integer.parseInt(operator);
                if (rentalNumber - 1 >= 0 && rentalNumber - 1 < userRentals.size()) {
                    Rental selectedRental = userRentals.get(rentalNumber - 1);
                    rentalService.returnVehicle(selectedRental.getId());
                    rentalService.getRentalRepo().save();
                    System.out.println("Vehicle (ID: " + selectedRental.getVehicleId() + ") returned successfully!");
                    return;
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'b' to step back.");
            }
            operator = getUserInput("Enter the number of the vehicle you want to return or b for step back: ");
        }
    }

    private static void getUserById() {
        String userId = getUserInput("Enter the ID of the user you want to view: ");
        User user = userService.getUserRepo().getUserById(userId);
        if (user == null) {
            System.out.println("User with ID " + userId + " does not exist!");
            return;
        }
        System.out.println(user);
    }

    private static void getVehicleById() {
        String vehicleId = getUserInput("Enter the ID of the vehicle you want to view: ");
        Vehicle vehicle = vehicleService.getVehicleRepo().getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            return;
        }
        System.out.println(vehicle);
    }

    private static boolean removeUser() {
        String userId = getUserInput("Enter the ID of the user you want to remove: ");
        User user = userService.getUserRepo().getUserById(userId);

        if (user == null) {
            System.out.println("User with ID " + userId + " does not exist!");
            return true;
        }

        List<Rental> rentals = rentalService.getRentalRepo().getByUserId(userId);
        if (!rentals.isEmpty()) {
            System.out.println("This user is renting at the moment. Are you sure (yes/no)? ");
            while (true) {
                String userInput = getUserInput("Enter your choice: ");
                if (userInput.equals("yes")) {
                    if (USER.getId().equals(userId)) {
                        System.out.println("You are trying to remove yourself. Are you sure (yes/no)? ");
                        while (true) {
                            userInput = getUserInput("Enter your choice: ");
                            if (userInput.equalsIgnoreCase("yes")) {
                                for (Rental r : rentals) {
                                    rentalService.getRentalRepo().remove(r.getId());
                                }
                                userService.getUserRepo().remove(userId);
                                System.out.println("Your account has been removed successfully!");
                                return false;
                            } else if (userInput.equalsIgnoreCase("no")) {
                                System.out.println("The operation was aborted!");
                                return true;
                            } else {
                                System.out.println("Invalid choice! Try again.");
                            }
                        }
                    } else {
                        for (Rental r : rentals) {
                            rentalService.getRentalRepo().remove(r.getId());
                        }
                        userService.getUserRepo().remove(userId);
                        rentalService.getRentalRepo().save();
                        System.out.println("User has been removed successfully!");
                        return false;
                    }
                } else if (userInput.equals("no")) {
                    System.out.println("The operation was aborted!");
                    return true;
                }
            }
        }
        return true;
    }

    private static void addVehicle() {
        String id = generateId();
        String category = getUserInput("Enter the category: ");
        String brand = getUserInput("Enter the brand: ");
        String model = getUserInput("Enter the model: ");
        int year = Integer.parseInt(getUserInput("Enter the year: "));
        String plate = getUserInput("Enter the plate number: ");
        double price = Double.parseDouble(getUserInput("Enter the price: "));

        Map<String, Object> attributes = new HashMap<>();
        while (true) {
            String addAttributesChoice = getUserInput("Do you want to add attributes? (yes/no): ").toLowerCase();
            if (addAttributesChoice.equals("yes")) {
                String attributeKey = getUserInput("Enter the name of an attribute: ");
                String attributeValue = getUserInput("Enter the value of the attribute: ");
                attributes.put(attributeKey, attributeValue);
            } else if (addAttributesChoice.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        }

        Vehicle newVehicle = new Vehicle(
                id,
                category,
                brand,
                model,
                year,
                plate,
                price,
                attributes
        );

        vehicleService.getVehicleRepo().add(newVehicle);
        System.out.println("Vehicle added successfully!");
    }

    private static void removeVehicle() {
        String vehicleId = getUserInput("Enter the ID of the vehicle you want to remove: ");
        Vehicle vehicle = vehicleService.getVehicleRepo().getVehicleById(vehicleId);

        if (vehicle == null) {
            System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            return;
        }

        for (Rental r : rentalService.getRentalRepo().getAll()) {
            if (r.getVehicleId().equals(vehicleId)) {
                System.out.println("This vehicle is currently being rented. Are you sure you want to remove it? (yes/no): ");
                while (true) {
                    String userInput = getUserInput("Enter your choice: ").toLowerCase();
                    if (userInput.equals("yes")) {
                        vehicleService.getVehicleRepo().delete(vehicleId);
                        rentalService.getRentalRepo().save();
                        System.out.println("Vehicle has been removed successfully!");
                        return;
                    } else if (userInput.equals("no")) {
                        System.out.println("The operation was aborted!");
                        return;
                    } else {
                        System.out.println("Invalid choice! Please type 'yes' or 'no'.");
                    }
                }
            }
        }
        vehicleService.getVehicleRepo().delete(vehicleId);
        System.out.println("Vehicle removed successfully!");
    }

    private static void getRentalById() {
        String rentalId = getUserInput("Enter the ID of the rental you want to view: ");
        Rental rental = rentalService.getRentalRepo().getById(rentalId);

        if (rental == null) {
            System.out.println("Rental with ID " + rentalId + " does not exist!");
            return;
        }
        System.out.println(rental);
    }

    private static void addRental() {
        String id = generateId();
        String vehicleId = getUserInput("Enter the vehicle ID: ");
        String userId = getUserInput("Enter the user ID: ");
        String rentDate = getUserInput("Enter the rent date (e.g., YYYY-MM-DD): ");
        String expirationDate = getUserInput("Enter the expiration date (e.g., YYYY-MM-DD): ");

        Rental newRental = new Rental(
                id,
                vehicleId,
                userId,
                rentDate,
                expirationDate
        );

        rentalService.getRentalRepo().add(newRental);
        System.out.println("Rental added successfully!");
    }

    private static void removeRental() {
        String rentalId = getUserInput("Enter the ID of the rental you want to remove: ");
        Rental rental = rentalService.getRentalRepo().getById(rentalId);

        if (rental == null) {
            System.out.println("Rental with ID " + rentalId + " does not exist!");
            return;
        }

        System.out.println("Are you sure you want to remove this rental? (yes/no): ");
        while (true) {
            String userInput = getUserInput("Enter your choice: ").toLowerCase();
            if (userInput.equals("yes")) {
                rentalService.getRentalRepo().remove(rentalId);
                System.out.println("Rental removed successfully!");
                return;
            } else if (userInput.equals("no")) {
                System.out.println("The operation was aborted!");
                return;
            } else {
                System.out.println("Invalid choice! Please type 'yes' or 'no'.");
            }
        }
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
