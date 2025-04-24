package org.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.impls.services.AttributesConverter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
public class Vehicle {
    @Id
    private String id;
    private String category;
    private String brand;
    private String model;
    private int year;
    private String plate;
    private double price;
    @Column(columnDefinition = "TEXT")
    @Convert(converter = AttributesConverter.class)
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
}
