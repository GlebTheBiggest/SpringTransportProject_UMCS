package org.example.models;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
public class Rental {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
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
}
