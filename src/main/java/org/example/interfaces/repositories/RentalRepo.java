package org.example.interfaces.repositories;

import org.example.models.Rental;
import java.util.List;

public interface RentalRepo {
    void remove(int id);
    List<Rental> getAll();
    Rental getById(int id);
    void add(Rental rental);
    boolean save();
    void read();
    List<Rental> getByUserId(int userId);
}
