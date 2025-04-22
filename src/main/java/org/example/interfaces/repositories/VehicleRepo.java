package org.example.interfaces.repositories;

import org.example.models.Vehicle;
import java.util.List;

public interface VehicleRepo {
    List<Vehicle> getAll();
    Vehicle getById(String id);
    boolean add(Vehicle vehicle);
    boolean remove(String id);
    void saveCsv();
    void readCsv();
    void saveJson();
    void readJson();
}
