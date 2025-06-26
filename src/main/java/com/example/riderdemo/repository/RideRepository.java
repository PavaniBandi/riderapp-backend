package com.example.riderdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.riderdemo.model.Ride;
import com.example.riderdemo.model.Ride.RideStatus;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByRiderUsername(String username);

    List<Ride> findByDriverUsername(String username);

    List<Ride> findByStatus(RideStatus status);

    List<Ride> findByDriverUsernameAndStatusNot(String driverUsername, RideStatus status);

}
