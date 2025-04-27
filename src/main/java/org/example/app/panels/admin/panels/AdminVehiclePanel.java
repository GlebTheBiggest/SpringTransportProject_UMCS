package org.example.app.panels.admin.panels;

import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.Rental;
import org.example.models.Vehicle;

import java.util.HashMap;
import java.util.Map;

import static org.example.impls.services.GlobalSavingService.ifSave;
import static org.example.impls.services.input.InputService.*;
import static org.example.security.IdGenerator.generateId;

public class AdminVehiclePanel {
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;

    public AdminVehiclePanel(VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public boolean start() {
        while (true) {
            System.out.println("VEHICLE MANAGEMENT MENU");
            System.out.println("""
                    1 - Get all vehicles
                    2 - Get vehicle by ID
                    3 - Add vehicle
                    4 - Remove vehicle
                    5 - Remove all vehicles
                    b - Step back
                    q - Log out""");
            char operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', '5', 'b', 'q'});
            switch (operator) {
                case '1' -> fetchAllVehicles();
                case '2' -> getVehicleById();
                case '3' -> addVehicle();
                case '4' -> removeVehicle();
                case '5' -> removeAllVehicles();
                case 'b' -> {
                    return true;
                }
                case 'q' -> {
                    ifSave();
                    System.out.println("Logging out...");
                    return false;
                }
            }
        }
    }

    private void removeAllVehicles() {
        if (vehicleService.getVehicleRepo().getAll().isEmpty()) {
            System.out.println("There is no vehicles to remove!");
            return;
        }
        if (!rentalService.getRentalRepo().getAll().isEmpty()) {
            System.out.println("That vehicles are rented now:");
            for (Rental rental : rentalService.getRentalRepo().getAll()) {
                System.out.println(rental.getVehicleId());
            }
        }
        if (!confirmAction("Are you sure (yes/no)? ")) {
            vehicleService.getVehicleRepo().removeAll();
            System.out.println("Vehicles have been removed successfully!");
        }
    }

    private void fetchAllVehicles() {
        try {
            vehicleService.printAllVehicles();
        } catch (Exception e) {
            System.out.println("An error occurred while fetching vehicles: " + e.getMessage());
        }
    }

    private void getVehicleById() {
        String vehicleId = getUserInput("Enter the ID of the vehicle you want to view: ");
        if (vehicleId.isBlank()) {
            System.out.println("Invalid or empty vehicle ID!");
            return;
        }
        try {
            Vehicle vehicle = vehicleService.getVehicleRepo().getById(vehicleId);
            if (vehicle == null) {
                System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
            } else {
                System.out.println(vehicle);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching the vehicle: " + e.getMessage());
        }
    }

    private void addVehicle() {
        try {
            String id = generateId();
            String category = getValidatedInput("Enter the category: ");
            String brand = getValidatedInput("Enter the brand: ");
            String model = getValidatedInput("Enter the model: ");
            int year = validateYear(getValidatedInput("Enter the year: "));
            String plate = validatePlate(getValidatedInput("Enter the plate number: "));
            double price = validatePrice(getValidatedInput("Enter the price: "));

            Map<String, Object> attributes = getVehicleAttributes();

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
            vehicleService.getVehicleRepo().saveCsv();
            System.out.println("Vehicle added successfully!");
        } catch (Exception e) {
            System.out.println("An error occurred while adding the vehicle: " + e.getMessage());
        }
    }

    private Map<String, Object> getVehicleAttributes() {
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
        return attributes;
    }

    private void removeVehicle() {
        String vehicleId = getUserInput("Enter the ID of the vehicle you want to remove: ");
        if (vehicleId.isBlank()) {
            System.out.println("Invalid or empty vehicle ID!");
            return;
        }
        try {
            Vehicle vehicle = vehicleService.getVehicleRepo().getById(vehicleId);
            if (vehicle == null) {
                System.out.println("Vehicle with ID " + vehicleId + " does not exist!");
                return;
            }

            boolean isRented = rentalService.getRentalRepo().getAll().stream()
                    .anyMatch(r -> r.getVehicleId().equals(vehicleId));
            if (isRented && !confirmAction("This vehicle is currently being rented. Are you sure you want to remove it (yes/no)? ")) {
                System.out.println("The operation was aborted!");
                return;
            }

            vehicleService.getVehicleRepo().remove(vehicleId);
            vehicleService.getVehicleRepo().saveCsv();
            System.out.println("Vehicle removed successfully!");
        } catch (Exception e) {
            System.out.println("An error occurred while removing the vehicle: " + e.getMessage());
        }
    }

    private String getValidatedInput(String prompt) {
        while (true) {
            String input = getUserInput(prompt);
            if (!input.isBlank()) {
                return input;
            }
            System.out.println("Invalid or empty input. Please try again.");
        }
    }

    private int validateYear(String input) {
        try {
            int year = Integer.parseInt(input);
            if (year > 1886 && year <= java.time.Year.now().getValue()) {
                return year;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid year input. Please try again.");
        }
        throw new IllegalArgumentException("Invalid year. Please provide a valid year.");
    }

    private String validatePlate(String input) {
        if (input.matches("[A-Z0-9-]+")) {
            return input;
        }
        throw new IllegalArgumentException("Invalid plate number. Please provide a valid plate number.");
    }

    private double validatePrice(String input) {
        try {
            double price = Double.parseDouble(input);
            if (price > 0) {
                return price;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Please provide a valid price.");
        }
        throw new IllegalArgumentException("Invalid price. Please provide a positive number.");
    }
}