package org.example.impls.services.csvService.reading;

import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;

public class CsvReadingService {
    private final UserRepoService userService;
    private final VehicleRepoService vehicleService;
    private final RentalRepoService rentalService;

    public CsvReadingService(UserRepoService userService, VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void read() {
        userService.getUserRepo().readCsv();
        vehicleService.getVehicleRepo().readCsv();
        rentalService.getRentalRepo().readCsv();
    }
}
