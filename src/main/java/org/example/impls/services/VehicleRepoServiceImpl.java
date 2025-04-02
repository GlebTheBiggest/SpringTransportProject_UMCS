package org.example.impls.services;

import org.example.interfaces.repositories.VehicleRepo;
import org.example.interfaces.services.VehicleRepoService;
import org.example.models.abstractions.Vehicle;

public class VehicleRepoServiceImpl implements VehicleRepoService {
    private final VehicleRepo vehicleRepo;

    public VehicleRepoServiceImpl(VehicleRepo vehicleRepo) {
        if (vehicleRepo == null) {
            throw new IllegalArgumentException("VehicleRepo cannot be null");
        }
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public void printAllVehicles() {
        vehicleRepo.getAll().forEach(System.out::println);
    }

    @Override
    public VehicleRepo getVehicleRepo() {
        return this.vehicleRepo;
    }

    @Override
    public void getAllAvailableVehicles() {
        for (Vehicle vehicle : vehicleRepo.getAll()) {
            if (!vehicle.isRented()) {
                System.out.println(vehicle);
            }
        }
    }

    @Override
    public void rentVehicle(int id) {
        Vehicle vehicle = vehicleRepo.getVehicleById(id);
        vehicle.setRented(true);
        vehicleRepo.save();
    }

    @Override
    public void returnVehicle(int id) {
        Vehicle vehicle = vehicleRepo.getVehicleById(id);
        vehicle.setRented(false);
        vehicleRepo.save();
    }
}
