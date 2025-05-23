package com.example.pearllogistics.EmployeeManagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image")
    private byte[] image;
    private String name;
    private String email;
    private String country;
    private int contactno;
    private String description;
    private String address;
    private String status  = "pending";
    private String role ;
    private int salary ;
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dJoined;
    private String UserType = "EMPLOYEE";
}
