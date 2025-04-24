package org.example.impls.services.repoServices;

import lombok.Data;
import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.Rental;
import org.example.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

@Data
public class VehicleRepoServiceImpl implements VehicleRepoService {
    private final VehicleRepo vehicleRepo;

    public VehicleRepoServiceImpl(VehicleRepo vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public void printAllVehicles() {
        if (vehicleRepo.getAll().isEmpty()) {
            System.out.println("No vehicles found!");
        } else {
            vehicleRepo.getAll().forEach(System.out::println);
        }
    }

    @Override
    public List<Vehicle> getAllAvailableVehicles(RentalRepo rentalRepo) {
        List<Vehicle> vehicles = new ArrayList<>();
        if (rentalRepo.getAll().isEmpty() && !vehicleRepo.getAll().isEmpty()) {
            vehicles.addAll(vehicleRepo.getAll());
        }
        for (Vehicle vehicle : vehicleRepo.getAll()) {
            for (Rental rental : rentalRepo.getAll()) {
                if (!vehicle.getId().equals(rental.getVehicleId())) {
                    vehicles.add(vehicle);
                }
            }
        }
        return vehicles;
    }
}
