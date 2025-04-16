package org.example.interfaces.services;

import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.models.Vehicle;

import java.util.List;

public interface VehicleRepoService {
    void printAllVehicles();
    VehicleRepo getVehicleRepo();
    List<Vehicle> getAllAvailableVehicles(RentalRepo rentalRepo);
}
