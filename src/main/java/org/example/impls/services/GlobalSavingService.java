package org.example.impls.services;

import lombok.Data;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;

@Data
public class GlobalSavingService {
    private UserRepoService userService;
    private VehicleRepoService vehicleService;
    private RentalRepoService rentalService;

    public GlobalSavingService(UserRepoService userService) {
        this.userService = userService;
    }

    public GlobalSavingService(VehicleRepoService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public GlobalSavingService(RentalRepoService rentalService) {
        this.rentalService = rentalService;
    }

    public GlobalSavingService(UserRepoService userService, VehicleRepoService vehicleService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    public GlobalSavingService(UserRepoService userService, RentalRepoService rentalService) {
        this.userService = userService;
        this.rentalService = rentalService;
    }

    public GlobalSavingService(VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public GlobalSavingService(UserRepoService userService, VehicleRepoService vehicleService, RentalRepoService rentalService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void save() {
        new CsvSavingService(userService, vehicleService, rentalService).saveCsv();
        new JsonSavingService(userService, vehicleService, rentalService).saveJson();
    }
}
