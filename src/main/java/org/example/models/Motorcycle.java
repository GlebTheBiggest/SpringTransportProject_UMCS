package org.example.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.models.abstractions.Vehicle;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class Motorcycle extends Vehicle {
    private String category;

    public Motorcycle(int id, String brand, String model, int year, int price, boolean rented, String category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }

    public Motorcycle(String brand, String model, int year, int price, boolean rented, String category) {
        super(brand, model, year, price, rented);
        this.category = category;
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + category;
    }

    @Override
    public Motorcycle cloneVehicle() {
        return new Motorcycle(getId(), getBrand(), getModel(), getYear(), getPrice(), isRented(), getCategory());
    }

}
