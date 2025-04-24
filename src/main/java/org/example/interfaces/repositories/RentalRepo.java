package org.example.interfaces.repositories;

import org.example.models.Rental;
import java.util.List;
import java.util.Optional;

public interface RentalRepo {
    boolean remove(String id);
    List<Rental> getAll();
    Rental getById(String id);
    boolean add(Rental rental);
    void saveCsv();
    void readCsv();
    List<Rental> getByUserId(String userId);
    void saveJson();
    void readJson();
    Optional<Rental> findByVehicleIdAndReturnDateIsNull(String id);
    void removeAll();
}
