package org.example.interfaces.repositories;

import org.example.models.Rental;
import java.util.List;

public interface RentalRepo {
    boolean remove(String id);
    List<Rental> getAll();
    Rental getById(String id);
    boolean add(Rental rental);
    boolean save();
    void read();
    List<Rental> getByUserId(String userId);
}
