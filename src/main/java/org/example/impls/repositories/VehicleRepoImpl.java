package org.example.impls.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.dao.VehicleDao;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.models.Vehicle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import static org.example.app.utils.HibernateUtil.getSessionFactory;

public class VehicleRepoImpl implements VehicleRepo {
    private static final String CSV_FILE_NAME = "vehicles.csv";
    private static final String JSON_FILE_NAME = "vehicles.json";
    private final VehicleDao vehicleDao;

    public VehicleRepoImpl() {
        this.vehicleDao = new VehicleDao(getSessionFactory());
    }

    @Override
    public List<Vehicle> getAll() {
        return vehicleDao.findAll();
    }

    @Override
    public Vehicle getById(String id) {
        return vehicleDao.findById(id);
    }

    @Override
    public boolean add(Vehicle vehicle) {
        if (vehicleDao.findById(vehicle.getId()) != null) {
            return false;
        }
        vehicleDao.save(vehicle);
        return true;
    }

    @Override
    public boolean remove(String id) {
        Vehicle vehicle = vehicleDao.findById(id);
        if (vehicle == null) {
            return false;
        }
        vehicleDao.delete(vehicle);
        return true;
    }

    @Override
    public void removeAll() {
        vehicleDao.deleteAll();
    }

    @Override
    public void saveCsv() {
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(CSV_FILE_NAME);

        for (Vehicle v : vehicleDao.findAll()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Vehicle(")
                    .append("id=").append(v.getId()).append("; ")
                    .append("category=").append(v.getCategory()).append("; ")
                    .append("brand=").append(v.getBrand()).append("; ")
                    .append("model=").append(v.getModel()).append("; ")
                    .append("year=").append(v.getYear()).append("; ")
                    .append("plate=").append(v.getPlate()).append("; ")
                    .append("price=").append(v.getPrice()).append("; ")
                    .append("attributes=").append(v.getAttributes().isEmpty() ? "{}" : v.getAttributes().toString())
                    .append(")");
            lines.add(sb.toString());
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public void readCsv() {
        Path path = Paths.get(CSV_FILE_NAME);

        try (Scanner input = new Scanner(path, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.startsWith("Vehicle(") || !line.endsWith(")")) {
                    System.err.println("Invalid format: " + line);
                    continue;
                }

                line = line.substring(8, line.length() - 1); // Видаляємо "Vehicle(" та ")"
                String[] parts = line.split("; ");
                String id = null, category = null, brand = null, model = null, plate = null;
                int year = 0;
                double price = 0.0;
                Map<String, Object> attributes = new HashMap<>();

                for (String part : parts) {
                    String[] keyValue = part.split("=", 2);
                    if (keyValue.length == 2) {
                        switch (keyValue[0].trim()) {
                            case "id" -> id = keyValue[1].trim();
                            case "category" -> category = keyValue[1].trim();
                            case "brand" -> brand = keyValue[1].trim();
                            case "model" -> model = keyValue[1].trim();
                            case "year" -> year = Integer.parseInt(keyValue[1].trim());
                            case "plate" -> plate = keyValue[1].trim();
                            case "price" -> price = Double.parseDouble(keyValue[1].trim());
                            case "attributes" -> {
                                if (!keyValue[1].trim().equals("{}")) {
                                    String attributesString = keyValue[1].trim().substring(1, keyValue[1].trim().length() - 1);
                                    String[] attributePairs = attributesString.split(", ");
                                    for (String pair : attributePairs) {
                                        String[] attributeKeyValue = pair.split("=");
                                        if (attributeKeyValue.length == 2) {
                                            attributes.put(attributeKeyValue[0], attributeKeyValue[1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Vehicle vehicle = new Vehicle(id, category, brand, model, year, plate, price, attributes);
                Vehicle existingVehicle = vehicleDao.findById(id);

                if (existingVehicle != null) {
                    vehicleDao.update(vehicle);
                } else {
                    vehicleDao.save(vehicle);
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
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), vehicleDao.findAll());
        } catch (IOException e) {
            System.err.println("Error saving JSON file: " + e.getMessage());
        }
    }

    @Override
    public void readJson() {
        Path path = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Vehicle> loadedVehicles = objectMapper.readValue(
                    path.toFile(),
                    new TypeReference<>() {}
            );

            for (Vehicle vehicle : loadedVehicles) {
                Vehicle existingVehicle = vehicleDao.findById(vehicle.getId());

                if (existingVehicle != null) {
                    vehicleDao.update(vehicle);
                } else {
                    vehicleDao.save(vehicle);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }
}