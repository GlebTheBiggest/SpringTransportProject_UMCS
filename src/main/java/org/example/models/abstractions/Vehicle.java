package org.example.models.abstractions;

import java.util.Objects;

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
        this(0, brand, model, year, price, rented); // ID = -1 для автоприсвоєння
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id && year == vehicle.year && price == vehicle.price && rented == vehicle.rented &&
                Objects.equals(brand, vehicle.brand) && Objects.equals(model, vehicle.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, year, price, rented);
    }

    public String toCsv() {
        return id + "," + brand + "," + model + "," + year + "," + price + "," + rented; // Використовуємо кому
    }

    public abstract Vehicle cloneVehicle();

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", rented=" + rented +
                '}';
    }
}
