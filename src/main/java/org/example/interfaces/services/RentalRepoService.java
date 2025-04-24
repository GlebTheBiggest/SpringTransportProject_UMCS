package org.example.interfaces.services;

import org.example.interfaces.repositories.RentalRepo;

public interface RentalRepoService {
    RentalRepo getRentalRepo();
    void printAllRentals();
    void rentVehicle(String vehicleId, String userId, String rentDate, String expirationDate);
    void returnVehicle(String id);
}