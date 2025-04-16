package org.example.interfaces.services;

import org.example.interfaces.repositories.RentalRepo;

public interface RentalRepoService {
    void printAllRentals();
    RentalRepo getRentalRepo();
    boolean rentVehicle(String vehicleId, String userId, String rentDate, String expirationDate);
    boolean returnVehicle(String id);
}