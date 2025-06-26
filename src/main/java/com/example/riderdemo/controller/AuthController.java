package com.example.riderdemo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.riderdemo.model.User;
import com.example.riderdemo.repository.UserRepository;
import com.example.riderdemo.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "https://riderapp-frontend.vercel.app")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        return "Signup successful!";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword()));

            User dbUser = userRepo.findByUsername(user.getUsername()).orElseThrow();

            String roleString = "ROLE_" + dbUser.getRole().name(); // e.g., ROLE_DRIVER
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    dbUser.getUsername(),
                    dbUser.getPassword(),
                    List.of(new SimpleGrantedAuthority(roleString))
            );

            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(dbUser.getRole().name() + ":" + token);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
