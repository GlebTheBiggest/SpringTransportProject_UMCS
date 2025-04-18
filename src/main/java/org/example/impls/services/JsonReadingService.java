package org.example.impls.services;

import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;

public class JsonReadingService {
    private final UserRepoService userService;
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;

    public JsonReadingService(UserRepoService userService, VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void read() {
        userService.getUserRepo().readJson();
        vehicleService.getVehicleRepo().readJson();
        rentalService.getRentalRepo().readJson();
    }
}
