package org.example.impls.repositories;

import org.example.interfaces.repositories.RentalRepo;
import org.example.models.Rental;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class RentalRepoImpl implements RentalRepo {
    private final List<Rental> rentals;
    private static final String FILE_NAME = "rentals.csv";

    public RentalRepoImpl() {
        this.rentals = new ArrayList<>();
        Path path = Paths.get(FILE_NAME);
        if (Files.exists(path)) {
            read();
        }
    }

    @Override
    public boolean add(Rental rental) {
        for (Rental r : this.rentals) {
            if (rental.getId().equals(r.getId())) {
                return false;
            }
        }
        this.rentals.add(rental);
        save();
        return true;
    }

    @Override
    public boolean remove(String id) {
        boolean removed = this.rentals.removeIf(rental -> String.valueOf(rental.getId()).equals(id));
        if (removed) {
            return save();
        }
        return false;
    }

    @Override
    public Rental getById(String id) {
        return this.rentals.stream()
                .filter(rental -> String.valueOf(rental.getId()).equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Rental> getAll() {
        List<Rental> rentalsCopy = new ArrayList<>();
        for (Rental rental : this.rentals) {
            rentalsCopy.add(rental.cloneRental());
        }
        return rentalsCopy;
    }

    @Override
    public List<Rental> getByUserId(String userId) {
        List<Rental> userRentals = new ArrayList<>();
        for (Rental rental : this.rentals) {
            if (String.valueOf(rental.getUserId()).equals(userId)) {
                userRentals.add(rental.cloneRental());
            }
        }
        return userRentals;
    }

    @Override
    public boolean save() {
        Path file = Paths.get(FILE_NAME);
        List<String> lines = new ArrayList<>();
        for (Rental rental : this.rentals) {
            lines.add(rental.toCsv());
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void read() {
        Path path = Paths.get(FILE_NAME);
        try (Scanner input = new Scanner(path, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 5) {
                    System.err.println("Invalid data format: " + line);
                    continue;
                }
                try {
                    String id = data[0];
                    String userId = data[1];
                    String vehicleId = data[2];
                    String rentalDate = data[3];
                    String expirationDate = data[4];
                    rentals.add(new Rental(id, userId, vehicleId, rentalDate, expirationDate));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}