package com.example.pearllogistics.AdminManagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    @Column(name = "image")
    private byte[] image;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dJoined;
    private String UserType = "ADMIN";


}

