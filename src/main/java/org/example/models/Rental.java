package org.example.models;

import lombok.Data;

@Data

public class Rental {
    private int id;
    private int vehicleId;
    private int userId;
    private String rentDate;
    private String expirationDate;

    public Rental(int id, int vehicleId, int userId, String rentDate, String expirationDate) {
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
    }}
