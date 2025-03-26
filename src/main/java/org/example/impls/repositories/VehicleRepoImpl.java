package org.example.impls.repositories;

import org.example.interfaces.repositories.VehicleRepo;
import org.example.models.Car;
import org.example.models.Motorcycle;
import org.example.models.abstractions.Vehicle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class VehicleRepoImpl implements VehicleRepo {
    private final List<Vehicle> vehicles;
    private int id = 0;
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
    public Vehicle getVehicleById(int id) {
        return this.vehicles.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void add(Vehicle vehicle) {
        if (vehicle.getId() == 0) {
            vehicle.setId(++id);
        }
        this.vehicles.add(vehicle);
        save();
    }

    @Override
    public void delete(int id) {
        boolean removed = this.vehicles.removeIf(v -> v.getId() == id);
        if (removed) {
            save();
        }
    }

    @Override
    public boolean save() {
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(FILE_NAME);

        for (Vehicle v : this.vehicles) {
            lines.add((v instanceof Car ? "Car:" : "Motorcycle:") + v.toCsv());
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void read() {
        Path path = Paths.get(FILE_NAME);
        int idLargest = 0;

        try (Scanner input = new Scanner(path, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(":");
                if (parts.length < 2) {
                    System.err.println("Invalid format: " + line);
                    continue;
                }
                String type = parts[0];
                String[] data = parts[1].split(",");
                try {
                    int id = Integer.parseInt(data[0]);
                    String brand = data[1];
                    String model = data[2];
                    int year = Integer.parseInt(data[3]);
                    int price = Integer.parseInt(data[4]);
                    boolean isRented = Boolean.parseBoolean(data[5]);

                    if (id > idLargest) {
                        idLargest = id;
                    }

                    if (type.equals("Car") && data.length == 6) {
                        vehicles.add(new Car(id, brand, model, year, price, isRented));
                    } else if (type.equals("Motorcycle") && data.length == 7) {
                        String category = data[6];
                        vehicles.add(new Motorcycle(id, brand, model, year, price, isRented, category));
                    } else {
                        System.err.println("Skipping invalid vehicle: " + line);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing vehicle: " + line);
                }
            }
            this.id = idLargest;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
