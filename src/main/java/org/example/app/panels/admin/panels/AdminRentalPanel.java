package org.example.app.panels.admin.panels;

import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.Rental;

import java.util.List;
import java.util.Optional;

import static org.example.impls.services.GlobalSavingService.ifSave;
import static org.example.impls.services.input.InputService.*;
import static org.example.security.IdGenerator.generateId;

public class AdminRentalPanel {
    private final UserRepoService userService;
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;

    public AdminRentalPanel(UserRepoService userService, VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public boolean start() {
        while (true) {
            System.out.println("RENTAL MANAGEMENT MENU");
            System.out.println("""
                    1 - Get all rentals
                    2 - Get rental by ID
                    3 - Add rental
                    4 - Remove rental
                    5 - Remove all rentals
                    6 - Get expired rentals
                    b - Step back
                    q - Log out""");
            char operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', '5', '6', 'b', 'q'});
            switch (operator) {
                case '1' -> rentalService.printAllRentals();
                case '2' -> getRentalById();
                case '3' -> addRental();
                case '4' -> removeRental();
                case '5' -> removeAllRentals();
                case '6' -> findOverdueRentals();
                case 'b' -> {
                    return true;
                }
                case 'q' -> {
                    ifSave(userService, vehicleService, rentalService);
                    System.out.println("Logging out...");
                    return false;
                }
            }
        }
    }

    private void findOverdueRentals() {
        Optional<List<Rental>> overdueRentals = rentalService.getRentalRepo().findOverdueRentals();
        if (overdueRentals.isEmpty()) {
            System.out.println("Expired rentals were not found");
        } else {
            System.out.println("Expired rentals: " + overdueRentals);
            if (confirmAction("Remove that rentals (yes/no)?: ")) {
                for (Rental rental : overdueRentals.get()) {
                    rentalService.getRentalRepo().remove(rental.getId());
                }
                System.out.println("Removed " + overdueRentals.get().size() + " rentals");
            }
        }
    }

    private void getRentalById() {
        String rentalId = getUserInput("Enter the ID of the rental you want to view: ");
        Rental rental = rentalService.getRentalRepo().getById(rentalId);

        if (rental == null) {
            System.out.println("Rental with ID " + rentalId + " does not exist!");
            return;
        }
        System.out.println(rental);
    }

    private void addRental() {
        String id = generateId();
        String vehicleId = getUserInput("Enter the vehicle ID: ");
        if (validateVehicleId(vehicleId)) {
            System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            return;
        }
        String userId = getUserInput("Enter the user ID: ");
        if (validateUserId(userId)) {
            System.out.println("User with ID " + userId + " does not exist!");
            return;
        }
        String rentDate = getUserInput("Enter the rent date (e.g., YYYY-MM-DD): ");
        if (validateDate(rentDate)) {
            System.out.println("Rent date is not valid!");
            return;
        }
        String expirationDate = getUserInput("Enter the expiration date (e.g., YYYY-MM-DD): ");
        if (validateDate(expirationDate) || !isExpirationAfterRent(rentDate, expirationDate)) {
            System.out.println("Expiration date is either invalid or before the rent date!");
            return;
        }

        Rental newRental = new Rental(
                id,
                vehicleId,
                userId,
                rentDate,
                expirationDate
        );

        try {
            rentalService.getRentalRepo().add(newRental);
            System.out.println("Rental added successfully!");
        } catch (Exception e) {
            System.out.println("An error occurred while adding the rental: " + e.getMessage());
        }
    }

    private void removeRental() {
        String rentalId = getUserInput("Enter the ID of the rental you want to remove: ");
        Rental rental = rentalService.getRentalRepo().getById(rentalId);

        if (rental == null) {
            System.out.println("Rental with ID " + rentalId + " does not exist!");
            return;
        }

        if (!confirmAction("Are you sure you want to remove this rental (yes/no)? ")) {
            System.out.println("The operation was aborted!");
            return;
        }

        try {
            rentalService.getRentalRepo().remove(rentalId);
            rentalService.getRentalRepo().saveCsv();
            System.out.println("Rental removed successfully!");
        } catch (Exception e) {
            System.out.println("An error occurred while removing the rental: " + e.getMessage());
        }
    }

    public void removeAllRentals() {
        if (rentalService.getRentalRepo().getAll().isEmpty()) {
            System.out.println("There is no rental to remove!");
        } else {
            rentalService.getRentalRepo().removeAll();
            System.out.println("Rentals have been removed successfully!");
        }
    }

    private boolean validateVehicleId(String vehicleId) {
        return vehicleService.getVehicleRepo().getById(vehicleId) == null;
    }

    private boolean validateUserId(String userId) {
        return userService.getUserRepo().getById(userId) == null;
    }

    private boolean validateDate(String date) {
        return !date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isExpirationAfterRent(String rentDate, String expirationDate) {
        return expirationDate.compareTo(rentDate) > 0;
    }
}