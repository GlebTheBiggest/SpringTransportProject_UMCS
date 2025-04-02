package org.example.models.abstractions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Vehicle {
    private int id;
    private String brand;
    private String model;
    private int year;
    private int price;
    private boolean rented;

    public Vehicle(int id, String brand, String model, int year, int price, boolean rented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
    }

    public Vehicle(String brand, String model, int year, int price, boolean rented) {
        this(0, brand, model, year, price, rented);
    }

    public String toCsv() {
        return id + "," + brand + "," + model + "," + year + "," + price + "," + rented;
    }

    public abstract Vehicle cloneVehicle();
}
