package org.example.impls.repositories;

import org.example.interfaces.repositories.RentalRepo;
import org.example.models.Rental;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class RentalRepoImpl implements RentalRepo {
    private final List<Rental> rentals;
    private int id = 0;
    private static final String FILE_NAME = "rentals.csv";

    public RentalRepoImpl() {
        this.rentals = new ArrayList<>();
        Path path = Paths.get(FILE_NAME);
        if (Files.exists(path)) {
            read();
        }
    }

    @Override
    public void add(Rental rental) {
        if (rental.getId() == 0) {
            rental.setId(++id);
        }
        this.rentals.add(rental);
        save();
    }

    @Override
    public void remove(int id) {
        boolean removed = this.rentals.removeIf(rental -> rental.getId() == id);
        if (removed) {
            save();
        }
    }

    @Override
    public Rental getById(int id) {
        return this.rentals.stream()
                .filter(rental -> rental.getId() == id)
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
    public List<Rental> getByUserId(int userId) {
        List<Rental> userRentals = new ArrayList<>();
        for (Rental rental : this.rentals) {
            if (rental.getUserId() == userId) {
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
        int idLargest = 0;
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
                    int id = Integer.parseInt(data[0]);
                    int userId = Integer.parseInt(data[1]);
                    int vehicleId = Integer.parseInt(data[2]);
                    String rentalDate = data[3];
                    String expirationDate = data[4];
                    rentals.add(new Rental(id, userId, vehicleId, rentalDate, expirationDate));
                    if (id > idLargest) {
                        idLargest = id;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
            this.id = idLargest;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
