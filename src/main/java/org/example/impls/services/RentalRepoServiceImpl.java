package org.example.impls.services;

import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.services.RentalRepoService;

public class RentalRepoServiceImpl implements RentalRepoService {
    private RentalRepo rentalRepo;

    @Override
    public void printAllRentals() {
        this.rentalRepo.getAll().forEach(System.out::println);
    }

    @Override
    public RentalRepo getRentalRepo() {
        return this.rentalRepo;
    }
}
