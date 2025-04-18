package org.example.app.panels.admin.panels;

import org.example.controllers.auth.Authentication;
import org.example.impls.services.*;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.User;

import static org.example.impls.services.InputService.confirmAction;
import static org.example.impls.services.InputService.getOperatorInput;

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
                    4 - Save in .csv
                    5 - Save in .json
                    6 - Save in database/////
                    7 - Read from .csv
                    8 - Read from .json
                    9 - Read from database/////
                    s - Save global
                    u - Your Account
                    q - Log out""");
            char operator = getOperatorInput("Enter your choice: ", new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 's', 'u', 'q'});
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
                    new CsvSavingService(userService, vehicleService, rentalService).saveCsv();
                    System.out.println("Data has been saved in .csv successfully!");
                }
                case '5' -> {
                    new JsonSavingService(userService, vehicleService, rentalService).saveJson();
                    System.out.println("Data has been saved in .json successfully!");
                }
                case '6' -> {
                    //
                }
                case '7' -> {
                    new CsvReadingService(userService, vehicleService, rentalService).read();
                    System.out.println("Data has been read from .csv successfully!");
                }
                case '8' -> {
                    new JsonReadingService(userService, vehicleService, rentalService).read();
                    System.out.println("Data has been read from .json successfully!");
                }
                case '9' -> {
                    //
                }
                case 's' -> {
                    new GlobalSavingService(userService, vehicleService, rentalService).save();
                    System.out.println("Data has been saved successfully!");
                }
                case 'u' -> {
                    System.out.println(USER.toString());
                }
                case 'q' -> {
                    if (confirmAction("Do you want to save changes (yes/no)?: ")) {
                        operator = getOperatorInput("1 - Save in .csv \n" +
                                                    "2 - Save in .json", new char[]{'1', '2'} );
                        if (operator == '1') {
                            new CsvSavingService(userService, vehicleService, rentalService).saveCsv();
                            System.out.println("Data has been saved in .csv successfully!");
                        } else {
                            new JsonSavingService(userService, vehicleService, rentalService).saveJson();
                            System.out.println("Data has been saved in .json successfully!");
                        }
                    }
                    return;
                }
            }
        }
    }


}
