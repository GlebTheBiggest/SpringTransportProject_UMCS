package org.example.impls.services.jsonService.saving;

import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;

public class JsonSavingService {
    private final UserRepoService userService;
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;

    public JsonSavingService(UserRepoService userService, VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void save() {
        userService.getUserRepo().saveJson();
        vehicleService.getVehicleRepo().saveJson();
        rentalService.getRentalRepo().saveJson();
    }
}
