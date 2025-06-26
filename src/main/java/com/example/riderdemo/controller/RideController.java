package com.example.riderdemo.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.riderdemo.model.Ride;
import com.example.riderdemo.service.RideService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/book")
    public Ride bookRide(@RequestBody RideRequest request,
            @AuthenticationPrincipal UserDetails user) {
        return rideService.bookRide(request.pickup, request.drop, user.getUsername());
    }

    @GetMapping("/available")
    public List<Ride> getAvailableRides(@AuthenticationPrincipal UserDetails user) {
        return rideService.getAvailableRides();
    }

    @GetMapping("/driver/dashboard")
    public List<Ride> getDriverDashboardRides(@AuthenticationPrincipal UserDetails user) {
        return rideService.getDashboardRidesForDriver(user.getUsername());
    }

    @PostMapping("/accept/{rideId}")
    public Ride acceptRide(@PathVariable Long rideId,
            @AuthenticationPrincipal UserDetails user) {
        return rideService.acceptRide(rideId, user.getUsername());
    }

    @GetMapping("/history")
    public List<Ride> getRideHistory(@AuthenticationPrincipal UserDetails user) {
        user.getAuthorities().forEach(auth -> System.out.println("Role: " + auth.getAuthority()));

        boolean isDriver = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DRIVER"));
        return rideService.getRidesForUser(user.getUsername(), isDriver);
    }

// @GetMapping("/driver/rides/available")
// public List<Ride> getDriverActiveRides(@AuthenticationPrincipal UserDetails user) {
//     return rideService.getAvailableRides();
// }
    @PutMapping("/ride/{id}/status")
    public Ride updateRideStatus(@PathVariable Long id, @RequestParam String status) {
        return rideService.updateStatus(id, status);
    }

    @PutMapping("/ride/{id}/pay")
    public Ride completePayment(@PathVariable Long id, @RequestParam String mode) {
        return rideService.completeRideWithPayment(id, mode);
    }

    @Data
    public static class RideRequest {

        private String pickup;
        private String drop;
    }
}
