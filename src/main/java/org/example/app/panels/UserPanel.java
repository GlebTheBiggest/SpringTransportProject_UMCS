package org.example.app.panels;

import org.example.impls.services.GlobalSavingService;
import org.example.impls.services.input.InputService;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class UserPanel {
    private final User user;
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;

    public UserPanel(User user, VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.user = user;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void start() {
        while (true) {
            System.out.println("\nUSER MENU");
            System.out.println("""
                    1 - Rent vehicle
                    2 - Return vehicle
                    3 - Your account
                    q - Log out""");
            char operator = InputService.getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', 'q'});
            switch (operator) {
                case '1' -> {
                    rentVehicle();
                    new GlobalSavingService(vehicleService, rentalService).save();
                }
                case '2' -> {
                    returnVehicle();
                    new GlobalSavingService(vehicleService, rentalService).save();
                }
                case '3' -> System.out.println(user);
                case 'q' -> {
                    System.out.println("Logging out...");
                    return;
                }
            }
        }
    }

    public void rentVehicle() {
        List<Vehicle> availableVehicles = vehicleService.getAllAvailableVehicles(rentalService.getRentalRepo());
        if (availableVehicles.isEmpty()) {
            System.out.println("There are no vehicles available!");
            return;
        }
        System.out.println("Available vehicles: \n");
        for (int i = 0; i < availableVehicles.size(); i++) {
            System.out.println((i + 1) + ": " + availableVehicles.get(i));
        }

        while (true) {
            String input = InputService.getUserInput("Enter the number of the vehicle you want to rent or b to go back: ");
            if (input.equalsIgnoreCase("b")) return;

            try {
                int vehicleNumber = Integer.parseInt(input);
                if (vehicleNumber - 1 < availableVehicles.size() && vehicleNumber - 1 >= 0) {
                    String rentDate = validateDate(InputService.getUserInput("Enter the date of the rent (e.g., YYYY-MM-DD): "));
                    String expirationDate = validateDate(InputService.getUserInput("Enter the expiration date of the rent (e.g., YYYY-MM-DD): "));

                    if (!isExpirationAfterRent(rentDate, expirationDate)) {
                        System.out.println("Expiration date must be after rent date. Please try again.");
                        continue;
                    }

                    rentalService.rentVehicle(availableVehicles.get(vehicleNumber - 1).getId(), user.getId(), rentDate, expirationDate);
                    rentalService.getRentalRepo().saveCsv();
                    System.out.println("Vehicle rented successfully!");
                    return;
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred while renting the vehicle: " + e.getMessage());
            }
        }
    }

    public void returnVehicle() {
        List<Rental> userRentals = new ArrayList<>();
        try {
            for (Rental r : rentalService.getRentalRepo().getAll()) {
                if (user.getId().equals(r.getUserId())) {
                    userRentals.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching rentals: " + e.getMessage());
            return;
        }

        if (userRentals.isEmpty()) {
            System.out.println("You have no rented vehicles to return!");
            return;
        }
        System.out.println("Your rented vehicles:\n");
        for (int i = 0; i < userRentals.size(); i++) {
            System.out.println((i + 1) + ": " + userRentals.get(i));
        }

        while (true) {
            String input = InputService.getUserInput("Enter the number of the vehicle to return or b to go back: ");
            if (input.equalsIgnoreCase("b")) return;

            try {
                int rentalNumber = Integer.parseInt(input);
                if (rentalNumber - 1 >= 0 && rentalNumber - 1 < userRentals.size()) {
                    Rental selectedRental = userRentals.get(rentalNumber - 1);
                    rentalService.returnVehicle(selectedRental.getId());
                    rentalService.getRentalRepo().saveCsv();
                    System.out.println("Vehicle returned successfully!");
                    return;
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred while returning the vehicle: " + e.getMessage());
            }
        }
    }

    private String validateDate(String input) {
        if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return input;
        }
        throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
    }

    private boolean isExpirationAfterRent(String rentDate, String expirationDate) {
        return expirationDate.compareTo(rentDate) > 0;
    }
}