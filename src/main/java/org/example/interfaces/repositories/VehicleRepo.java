package org.example.interfaces.repositories;

import org.example.models.Vehicle;
import java.util.List;

public interface VehicleRepo {
    List<Vehicle> getAll();
    Vehicle getVehicleById(String id);
    boolean add(Vehicle vehicle);
    boolean delete(String id);
    boolean save();
    void read();
}
