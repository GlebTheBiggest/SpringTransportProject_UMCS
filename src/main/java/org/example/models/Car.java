package org.example.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.models.abstractions.Vehicle;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class Car extends Vehicle {

    public Car(int id, String brand, String model, int year, int price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    public Car(String brand, String model, int year, int price, boolean rented) {
        super(brand, model, year, price, rented);
    }

    @Override
    public Car cloneVehicle() {
        return new Car(getId(), getBrand(), getModel(), getYear(), getPrice(), isRented());
    }
}
