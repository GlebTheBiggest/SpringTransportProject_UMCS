package org.example.impls.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.interfaces.repositories.RentalRepo;
import org.example.models.Rental;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class RentalRepoImpl implements RentalRepo {
    private final List<Rental> rentals;
    private static final String CSV_FILE_NAME = "rentals.csv";
    private static final String JSON_FILE_NAME = "rentals.json";


    public RentalRepoImpl() {
        this.rentals = new ArrayList<>();
        Path path = Paths.get(JSON_FILE_NAME);
        if (Files.exists(path)) {
            readJson();
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
        return true;
    }

    @Override
    public boolean remove(String id) {
        boolean removed = this.rentals.removeIf(rental -> String.valueOf(rental.getId()).equals(id));
        if (removed) {
            return saveCsv();
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
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        return rentals.stream()
                .filter(r -> r.getVehicleId().equals(vehicleId))
                .filter(r -> r.getExpirationDate() == null)
                .findFirst();
    }

    @Override
    public boolean saveCsv() {
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(CSV_FILE_NAME);

        for (Rental rental : this.rentals) {
            StringBuilder sb = new StringBuilder();
            sb.append("Rental(")
                    .append("id=").append(rental.getId()).append("; ")
                    .append("userId=").append(rental.getUserId()).append("; ")
                    .append("vehicleId=").append(rental.getVehicleId()).append("; ")
                    .append("rentalDate=").append(rental.getRentDate()).append("; ")
                    .append("expirationDate=").append(rental.getExpirationDate())
                    .append(")");
            lines.add(sb.toString());
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
    public void readCsv() {
        Path path = Paths.get(CSV_FILE_NAME);

        try (Scanner input = new Scanner(path, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) continue;

                try {
                    if (!line.startsWith("Rental(") || !line.endsWith(")")) {
                        System.err.println("Invalid format: " + line);
                        continue;
                    }

                    line = line.substring(7, line.length() - 1); // Видаляємо "Rental(" та ")"
                    String[] parts = line.split("; ");
                    String id = null, userId = null, vehicleId = null, rentalDate = null, expirationDate = null;

                    for (String part : parts) {
                        String[] keyValue = part.split("=", 2);
                        if (keyValue.length != 2) {
                            System.err.println("Invalid key-value format: " + part);
                            continue;
                        }

                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();

                        switch (key) {
                            case "id" -> id = value;
                            case "userId" -> userId = value;
                            case "vehicleId" -> vehicleId = value;
                            case "rentalDate" -> rentalDate = value;
                            case "expirationDate" -> expirationDate = value;
                        }
                    }

                    rentals.add(new Rental(id, userId, vehicleId, rentalDate, expirationDate));
                } catch (Exception e) {
                    System.err.println("Error parsing rental: " + line + ", reason: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public boolean saveJson() {
        Path file = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file.toFile(), this.rentals);
        } catch (IOException e) {
            System.err.println("Error saving JSON file: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void readJson() {
        Path file = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Rental> loadedRentals = objectMapper.readValue(file.toFile(), new TypeReference<List<Rental>>() {});
            this.rentals.clear();
            this.rentals.addAll(loadedRentals);
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}