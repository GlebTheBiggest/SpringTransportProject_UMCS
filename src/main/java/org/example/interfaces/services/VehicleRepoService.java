package org.example.interfaces.services;

import org.example.interfaces.repositories.VehicleRepo;

public interface VehicleRepoService {
    void printAllVehicles();
    void rentVehicle(int id);
    void returnVehicle(int id);
    VehicleRepo getVehicleRepo();
}
