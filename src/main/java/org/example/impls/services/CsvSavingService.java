package org.example.impls.services;

import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;

public class CsvSavingService {
    private final UserRepoService userService;
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;

    public CsvSavingService(UserRepoService userService, VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }
    public void saveCsv() {
        userService.getUserRepo().saveCsv();
        vehicleService.getVehicleRepo().saveCsv();
        rentalService.getRentalRepo().saveCsv();
    }
}
