package org.example.interfaces.services;

import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.models.Vehicle;

import java.util.List;

public interface VehicleRepoService {
    VehicleRepo getVehicleRepo();
    void printAllVehicles();
    List<Vehicle> getAllAvailableVehicles(RentalRepo rentalRepo);
}
