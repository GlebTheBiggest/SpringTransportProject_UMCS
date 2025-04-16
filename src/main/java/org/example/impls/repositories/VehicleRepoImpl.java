package org.example.impls.repositories;

import org.example.interfaces.repositories.VehicleRepo;
import org.example.models.Vehicle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class VehicleRepoImpl implements VehicleRepo {
    private final List<Vehicle> vehicles;
    private static final String FILE_NAME = "vehicles.csv";

    public VehicleRepoImpl() {
        this.vehicles = new ArrayList<>();
        Path path = Paths.get(FILE_NAME);
        if (Files.exists(path)) {
            read();
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
        if (vehicle.getId() == null || vehicle.getId().isEmpty()) {
            vehicle.setId(UUID.randomUUID().toString());
        }
        this.vehicles.add(vehicle);
        return save();
    }

    @Override
    public boolean delete(String id) {
        boolean removed = this.vehicles.removeIf(v -> v.getId().equals(id));
        if (removed) {
            return save();
        }
        return false;
    }

    @Override
    public boolean save() {
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(FILE_NAME);

        for (Vehicle v : this.vehicles) {
            lines.add(v.toCsv());
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
                if (data.length < 7) {
                    System.err.println("Invalid format: " + line);
                    continue;
                }
                try {
                    String id = data[0];
                    String category = data[1];
                    String brand = data[2];
                    String model = data[3];
                    int year = Integer.parseInt(data[4]);
                    String plate = data[5];
                    double price = Double.parseDouble(data[6]);
                    Map<String, Object> attributes = parseAttributes(data[7]);

                    vehicles.add(new Vehicle(id, category, brand, model, year, plate, price, attributes));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error parsing vehicle: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private Map<String, Object> parseAttributes(String attributesString) {
        Map<String, Object> attributes = new HashMap<>();
        String[] entries = attributesString.split(";");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                attributes.put(keyValue[0], keyValue[1]);
            }
        }
        return attributes;
    }
}