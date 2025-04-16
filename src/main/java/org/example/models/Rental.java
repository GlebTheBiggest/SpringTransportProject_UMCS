package org.example.models;

import lombok.Data;

@Data
public class Rental {
    private String id;
    private String vehicleId;
    private String userId;
    private String rentDate;
    private String expirationDate;

    public Rental(String id, String vehicleId, String userId, String rentDate, String expirationDate) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.rentDate = rentDate;
        this.expirationDate = expirationDate;
    }

    public String toCsv() {
        return id + "," + vehicleId + "," + userId + "," + rentDate + "," + expirationDate;
    }

    public Rental cloneRental() {
        return new Rental(id, vehicleId, userId, rentDate, expirationDate);
    }
}
