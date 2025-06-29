package com.example.riderdemo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    public enum Role {
        RIDER, DRIVER
    }
}
