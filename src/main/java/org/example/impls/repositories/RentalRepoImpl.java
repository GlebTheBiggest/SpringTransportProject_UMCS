package org.example.impls.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.dao.RentalDao;
import org.example.interfaces.repositories.RentalRepo;
import org.example.models.Rental;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

import static org.example.app.utils.HibernateUtil.getSessionFactory;

public class RentalRepoImpl implements RentalRepo {
    private static final String CSV_FILE_NAME = "rentals.csv";
    private static final String JSON_FILE_NAME = "rentals.json";
    private final RentalDao rentalDao;

    public RentalRepoImpl() {
        this.rentalDao = new RentalDao(getSessionFactory());
    }

    @Override
    public boolean add(Rental rental) {
        if (rentalDao.findById(rental.getId()) != null) {
            return false;
        }
        rentalDao.save(rental);
        return true;
    }

    @Override
    public boolean remove(String id) {
        Rental rental = rentalDao.findById(id);
        if (rental == null) {
            return false;
        }
        rentalDao.delete(rental);
        return true;
    }

    @Override
    public void removeAll() {
        rentalDao.deleteAll();
    }

    @Override
    public Rental getById(String id) {
        return rentalDao.findById(id);
    }

    @Override
    public List<Rental> getAll() {
        return rentalDao.findAll();
    }

    @Override
    public List<Rental> getByUserId(String userId) {
        return rentalDao.findAll().stream()
                .filter(rental -> rental.getUserId().equals(userId))
                .toList();
    }

    @Override
    public Optional<List<Rental>> findOverdueRentals() {
        LocalDate currentDate = LocalDate.now();
        List<Rental> overdueRentals = rentalDao.findAll().stream()
                .filter(rental -> {
                    if (rental.getExpirationDate() != null) {
                        LocalDate expirationDate = LocalDate.parse(rental.getExpirationDate());
                        return expirationDate.isBefore(currentDate);
                    }
                    return false;
                })
                .toList();
        return overdueRentals.isEmpty() ? Optional.empty() : Optional.of(overdueRentals);
    }

    @Override
    public void saveCsv() {
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(CSV_FILE_NAME);

        for (Rental rental : rentalDao.findAll()) {
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
            Files.write(file, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public void readCsv() {
        Path path = Paths.get(CSV_FILE_NAME);

        try (Scanner input = new Scanner(path)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.startsWith("Rental(") || !line.endsWith(")")) {
                    continue;
                }

                line = line.substring(7, line.length() - 1);
                String[] parts = line.split("; ");
                String id = null, userId = null, vehicleId = null, rentalDate = null, expirationDate = null;

                for (String part : parts) {
                    String[] keyValue = part.split("=", 2);
                    if (keyValue.length == 2) {
                        switch (keyValue[0].trim()) {
                            case "id" -> id = keyValue[1].trim();
                            case "userId" -> userId = keyValue[1].trim();
                            case "vehicleId" -> vehicleId = keyValue[1].trim();
                            case "rentalDate" -> rentalDate = keyValue[1].trim();
                            case "expirationDate" -> expirationDate = keyValue[1].trim();
                        }
                    }
                }

                Rental rental = new Rental(id, userId, vehicleId, rentalDate, expirationDate);
                Rental existingRental = rentalDao.findById(id);

                if (existingRental != null) {
                    rentalDao.update(rental);
                } else {
                    rentalDao.save(rental);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void saveJson() {
        Path file = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), rentalDao.findAll());
        } catch (IOException e) {
            System.err.println("Error saving JSON file: " + e.getMessage());
        }
    }

    @Override
    public void readJson() {
        Path path = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Rental> loadedRentals = objectMapper.readValue(
                    path.toFile(),
                    new TypeReference<>() {}
            );

            for (Rental rental : loadedRentals) {
                Rental existingRental = rentalDao.findById(rental.getId());

                if (existingRental != null) {
                    rentalDao.update(rental);
                } else {
                    rentalDao.save(rental);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }
}