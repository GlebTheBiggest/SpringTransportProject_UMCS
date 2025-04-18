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

    public String toCsv() {
        StringBuilder attrBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            attrBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        if (!attrBuilder.isEmpty()) {
            attrBuilder.append("{}");
        }

        return String.join(",",
                id,
                category,
                brand,
                model,
                String.valueOf(year),
                plate,
                String.valueOf(price),
                attrBuilder.toString()
        );
    }

    public Object getAttribute(String key) {
        return attributes != null ? attributes.get(key) : null;
    }

    public void addAttribute(String key, Object value) {
        if (attributes != null) {
            attributes.put(key, value);
        }
    }

    public void removeAttribute(String key) {
        if (attributes != null) {
            attributes.remove(key);
        }
    }
}
