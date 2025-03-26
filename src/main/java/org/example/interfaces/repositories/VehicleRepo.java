package org.example.interfaces.repositories;

import org.example.models.abstractions.Vehicle;
import java.util.List;

public interface VehicleRepo {
    List<Vehicle> getAll();
    Vehicle getVehicleById(int id);
    void add(Vehicle vehicle);
    void delete(int id);
    boolean save();
    void read();
}
