package org.example.impls.services.repoServices;

import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.services.RentalRepoService;
import org.example.models.Rental;

import static org.example.security.IdGenerator.generateId;

public class RentalRepoServiceImpl implements RentalRepoService {
    private final RentalRepo rentalRepo;

    public RentalRepoServiceImpl(RentalRepo rentalRepo) {
        if (rentalRepo == null) {
            throw new IllegalArgumentException("UserRepo cannot be null!");
        }
        this.rentalRepo = rentalRepo;
    }

    @Override
    public void printAllRentals() {
        if (rentalRepo.getAll().isEmpty()) {
            System.out.println("No rentals found!");
        } else {
            this.rentalRepo.getAll().forEach(System.out::println);
        }
    }

    @Override
    public RentalRepo getRentalRepo() {
        return this.rentalRepo;
    }

    @Override
    public boolean rentVehicle(String vehicleId, String userId, String rentDate, String expirationDate) {
        return this.rentalRepo.add(new Rental(generateId(), vehicleId, userId, rentDate, expirationDate));
    }

    @Override
    public boolean returnVehicle(String id) {
        return this.rentalRepo.remove(id);
    }
}
