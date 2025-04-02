package org.example.interfaces.services;

import org.example.interfaces.repositories.RentalRepo;

public interface RentalRepoService {
    void printAllRentals();
    RentalRepo getRentalRepo();
}