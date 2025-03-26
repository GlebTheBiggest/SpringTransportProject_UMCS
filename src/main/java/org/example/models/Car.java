package org.example.models;

import org.example.models.abstractions.Vehicle;

public class Car extends Vehicle {
    public Car(int id, String brand, String model, int year, int price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    public Car(String brand, String model, int year, int price, boolean rented) {
        super(brand, model, year, price, rented);
    }

    @Override
    public String toCsv() {
        return super.toCsv(); // Використання стандартного CSV-формату
    }

    @Override
    public Car cloneVehicle() {
        return new Car(getId(), getBrand(), getModel(), getYear(), getPrice(), isRented());
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + getId() +
                ", brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", year=" + getYear() +
                ", price=" + getPrice() +
                ", rented=" + isRented() +
                '}';
    }
}
