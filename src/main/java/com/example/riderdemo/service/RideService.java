package com.example.riderdemo.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.riderdemo.model.Ride;
import com.example.riderdemo.model.Ride.RideStatus;
import com.example.riderdemo.repository.RideRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepo;

    public Ride bookRide(String pickup, String drop, String riderUsername) {
        double fare = estimateFare(pickup, drop);
        int time = estimateTime(pickup, drop);

        Ride ride = new Ride();
        ride.setPickupLocation(pickup);
        ride.setDropLocation(drop);
        ride.setEstimatedFare(fare);
        ride.setEstimatedTime(time);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setRiderUsername(riderUsername);

        return rideRepo.save(ride);
    }

    private double estimateFare(String from, String to) {
        return 50 + Math.random() * 100; // Simulated logic
    }

    private int estimateTime(String from, String to) {
        return 10 + (int) (Math.random() * 20);
    }

    public List<Ride> getAvailableRides() {
        return rideRepo.findByStatus(Ride.RideStatus.REQUESTED);
    }

    public Ride acceptRide(Long rideId, String driverUsername) {
        Ride ride = rideRepo.findById(rideId).orElseThrow();
        if (ride.getStatus() != Ride.RideStatus.REQUESTED) {
            throw new IllegalStateException("Ride already accepted.");
        }
        ride.setStatus(Ride.RideStatus.ACCEPTED);
        ride.setDriverUsername(driverUsername);
        return rideRepo.save(ride);
    }

    public List<Ride> getRidesForUser(String username, boolean isDriver) {
        return isDriver ? rideRepo.findByDriverUsername(username)
                : rideRepo.findByRiderUsername(username);
    }

    public List<Ride> findRequestedRides() {
        return rideRepo.findByStatus(RideStatus.REQUESTED);
    }

    public Ride updateStatus(Long id, String status) {
        Ride ride = rideRepo.findById(id).orElseThrow();
        ride.setStatus(RideStatus.valueOf(status.toUpperCase()));
        return rideRepo.save(ride);
    }

    public Ride completeRideWithPayment(Long id, String paymentMode) {
        Ride ride = rideRepo.findById(id).orElseThrow();
        ride.setStatus(RideStatus.COMPLETED);
        // You can store paymentMode in DB if needed
        return rideRepo.save(ride);
    }
// public List<Ride> findRidesForDriver(String driverUsername) {
//     return rideRepo.findByDriverUsernameAndStatusNot(driverUsername, "COMPLETED");
// }

    public List<Ride> getDashboardRidesForDriver(String driverUsername) {
        List<Ride> assignedRides = rideRepo.findByDriverUsernameAndStatusNot(driverUsername, RideStatus.COMPLETED);
        List<Ride> requestedRides = rideRepo.findByStatus(RideStatus.REQUESTED);

        Set<Ride> result = new LinkedHashSet<>();
        result.addAll(requestedRides);
        result.addAll(assignedRides);
        return new ArrayList<>(result);
    }

}
