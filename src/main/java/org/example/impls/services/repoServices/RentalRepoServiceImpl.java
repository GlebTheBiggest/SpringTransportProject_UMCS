package org.example.impls.services.repoServices;

import lombok.Data;
import org.example.interfaces.repositories.RentalRepo;
import org.example.interfaces.services.RentalRepoService;
import org.example.models.Rental;

import static org.example.security.IdGenerator.generateId;

@Data
public class RentalRepoServiceImpl implements RentalRepoService {
    private final RentalRepo rentalRepo;

    public RentalRepoServiceImpl(RentalRepo rentalRepo) {
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
    public void rentVehicle(String vehicleId, String userId, String rentDate, String expirationDate) {
        this.rentalRepo.add(new Rental(generateId(), vehicleId, userId, rentDate, expirationDate));
    }

    @Override
    public void returnVehicle(String id) {
        this.rentalRepo.remove(id);
    }
}
