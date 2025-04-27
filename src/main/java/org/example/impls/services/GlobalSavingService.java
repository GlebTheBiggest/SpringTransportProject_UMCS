package org.example.impls.services;

import lombok.Data;
import org.example.impls.services.csvService.saving.CsvSavingService;
import org.example.impls.services.jsonService.saving.JsonSavingService;
import org.example.interfaces.services.RentalRepoService;
import org.example.interfaces.services.UserRepoService;
import org.example.interfaces.services.VehicleRepoService;

import static org.example.impls.services.input.InputService.confirmAction;

@Data
public class GlobalSavingService {
    private static UserRepoService userService;
    private static VehicleRepoService vehicleService;
    private static RentalRepoService rentalService;

    public GlobalSavingService(VehicleRepoService vehicleService, RentalRepoService rentalService) {
        GlobalSavingService.vehicleService = vehicleService;
        GlobalSavingService.rentalService = rentalService;
    }

    public void save() {
        if (userService != null && userService.getUserRepo() != null) {
            userService.getUserRepo().saveCsv();
            userService.getUserRepo().saveJson();
        }

        if (vehicleService != null && vehicleService.getVehicleRepo() != null) {
            vehicleService.getVehicleRepo().saveCsv();
            vehicleService.getVehicleRepo().saveJson();
        }

        if (rentalService != null && rentalService.getRentalRepo() != null) {
            rentalService.getRentalRepo().saveCsv();
            rentalService.getRentalRepo().saveJson();
        }
    }

    public static void ifSave() {
        if (confirmAction("Do you want to make a backup (yes/no)?: ")) {
            new CsvSavingService(userService, vehicleService, rentalService).save();
            System.out.println("Data has been saved in .csv successfully!");
            new JsonSavingService(userService, vehicleService, rentalService).save();
            System.out.println("Data has been saved in .json successfully!");
        }
    }
}
