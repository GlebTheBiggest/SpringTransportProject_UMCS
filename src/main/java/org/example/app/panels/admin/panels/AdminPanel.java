package org.example.app.panels.admin.panels;

import org.example.controllers.auth.Authentication;
import org.example.impls.services.csvService.reading.CsvReadingService;
import org.example.impls.services.csvService.saving.CsvSavingService;
import org.example.impls.services.jsonService.reading.JsonReadingService;
import org.example.impls.services.jsonService.saving.JsonSavingService;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.User;

import static org.example.impls.services.GlobalSavingService.ifSave;
import static org.example.impls.services.input.InputService.getOperatorInput;

public class AdminPanel {
    private final User USER;
    private final UserRepoService userService;
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;
    private final Authentication auth;

    public AdminPanel(User user, UserRepoService userService, VehicleRepoService vehicleService, RentalRepoService rentalService, Authentication auth) {
        this.USER = user;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.auth = auth;
    }

    public void start() {
        while (true) {
            System.out.println("ADMIN MENU");
            System.out.println("""
                    1 - User Management
                    2 - Vehicle Management
                    3 - Rental Management
                    4 - Make a backup
                    5 - Read from backup
                    u - Your Account
                    q - Log out""");
            char operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', '5', '6', '7', 's', 'u', 'q'});
            switch (operator) {
                case '1' -> {
                    if (!new AdminUserPanel(USER, userService, rentalService, auth).start()) {
                        return;
                    }
                }
                case '2' -> {
                    if (!new AdminVehiclePanel(vehicleService, rentalService).start()) {
                        return;
                    }
                }
                case '3' -> {
                    if (!new AdminRentalPanel(userService, vehicleService, rentalService).start()) {
                        return;
                    }
                }
                case '4' -> {
                    operator = getOperatorInput("Do you want to make backup in .csv, .json or both (c/j/b)? ", new char[]{'c', 'j', 'b'});
                    if (operator == 'c') {
                        new CsvSavingService(userService, vehicleService, rentalService).save();
                        System.out.println("Data has been saved in .csv successfully!");
                    } else if (operator == 'j') {
                        new JsonSavingService(userService, vehicleService, rentalService).save();
                        System.out.println("Data has been saved in .json successfully!");
                    } else {
                        new CsvSavingService(userService, vehicleService, rentalService).save();
                        new JsonSavingService(userService, vehicleService, rentalService).save();
                        System.out.println("The backup has been made successfully!");
                    }
                }
                case '5' -> {
                    operator = getOperatorInput("Do you want to read from .csv or .json (c/j)? ", new char[]{'c', 'j'});
                    if (operator == 'c') {
                        new CsvReadingService(userService, vehicleService, rentalService).read();
                        System.out.println("Data has been read from.csv successfully!");
                    } else {
                        new JsonReadingService(userService, vehicleService, rentalService).read();
                        System.out.println("Data has been read from .json successfully!");
                    }
                }
                case 'u' -> System.out.println(USER.toString());
                case 'q' -> {
                    ifSave();
                    System.out.println("Logging out...");
                    return;
                }
            }
        }
    }
}
