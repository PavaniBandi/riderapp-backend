package com.example.riderdemo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pickupLocation;
    private String dropLocation;

    private double estimatedFare;
    private int estimatedTime; // in minutes

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private String riderUsername;
    private String driverUsername;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RideStatus {
    REQUESTED,
    ACCEPTED,
    PICKED,
    COMPLETED
}
}
