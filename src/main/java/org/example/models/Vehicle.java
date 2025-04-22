package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Vehicle {
    private String id;
    private String category;
    private String brand;
    private String model;
    private int year;
    private String plate;
    private double price;
    private Map<String, Object> attributes = Map.of();


    public Vehicle(String id, String category, String brand, String model, int year, String plate, double price, Map<String, Object> attributes) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.plate = plate;
        this.price = price;
        this.attributes = attributes;
    }

    public Vehicle cloneVehicle() {
        return new Vehicle(
                id,
                category,
                brand,
                model,
                year,
                plate,
                price,
                new HashMap<>(attributes)
        );
    }
}
