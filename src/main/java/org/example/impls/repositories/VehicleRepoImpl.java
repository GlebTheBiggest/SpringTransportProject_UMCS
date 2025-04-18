package org.example.impls.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.interfaces.repositories.VehicleRepo;
import org.example.models.Vehicle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class VehicleRepoImpl implements VehicleRepo {
    private final List<Vehicle> vehicles;
    private static final String CSV_FILE_NAME = "vehicles.csv";
    private static final String JSON_FILE_NAME = "vehicles.json";

    public VehicleRepoImpl() {
        this.vehicles = new ArrayList<>();
        Path path = Paths.get(JSON_FILE_NAME);
        if (Files.exists(path)) {
            readJson();
        }
    }

    @Override
    public List<Vehicle> getAll() {
        List<Vehicle> vehiclesCopy = new ArrayList<>();
        for (Vehicle vehicle : this.vehicles) {
            vehiclesCopy.add(vehicle.cloneVehicle());
        }
        return vehiclesCopy;
    }

    @Override
    public Vehicle getVehicleById(String id) {
        return this.vehicles.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean add(Vehicle vehicle) {
        for (Vehicle v : this.vehicles) {
            if (vehicle.getId().equals(v.getId())) {
                return false;
            }
        }
        this.vehicles.add(vehicle);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return this.vehicles.removeIf(v -> v.getId().equals(id));
    }

    @Override
    public boolean saveCsv() {
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(CSV_FILE_NAME);

        for (Vehicle v : this.vehicles) {
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
                        if (keyValue.length != 2) {
                            System.err.println("Invalid key-value format: " + part);
                            continue;
                        }

                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();

                        switch (key) {
                            case "id" -> id = value;
                            case "category" -> category = value;
                            case "brand" -> brand = value;
                            case "model" -> model = value;
                            case "year" -> year = Integer.parseInt(value);
                            case "plate" -> plate = value;
                            case "price" -> price = Double.parseDouble(value);
                            case "attributes" -> {
                                if (!value.equals("{}")) {
                                    String attributesString = value.substring(1, value.length() - 1); // Видаляємо "{}"
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

                    vehicles.add(new Vehicle(id, category, brand, model, year, plate, price, attributes));
                } catch (Exception e) {
                    System.err.println("Error parsing vehicle: " + line + ", reason: " + e.getMessage());
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
            objectMapper.writeValue(file.toFile(), this.vehicles);
        } catch (IOException e) {
            System.err.println("Error saving JSON file: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void readJson() {
        Path path = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Vehicle> loadedVehicles = objectMapper.readValue(path.toFile(), new TypeReference<List<Vehicle>>() {});
            this.vehicles.clear();
            this.vehicles.addAll(loadedVehicles);
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}