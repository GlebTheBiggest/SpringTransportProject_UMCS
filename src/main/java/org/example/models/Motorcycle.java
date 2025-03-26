package org.example.models;

import org.example.models.abstractions.Vehicle;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + category; // Однаковий формат CSV
    }

    @Override
    public Motorcycle cloneVehicle() {
        return new Motorcycle(getId(), getBrand(), getModel(), getYear(), getPrice(), isRented(), getCategory());
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "id=" + getId() +
                ", brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", year=" + getYear() +
                ", price=" + getPrice() +
                ", rented=" + isRented() +
                ", category='" + category + '\'' +
                '}';
    }
}
