//package com.example.pearllogistics.OrderManagement.Entity;
//
//import com.example.pearllogistics.UserManagement.Entity.UserEntity;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "test")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class OrderEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private UserEntity user;
//
//
////Allowing the following fields to be null temporarily
//    @Column(nullable = true) // Allow null values
//    private String productName;
//
//    @Column(nullable = true) // Allow null values
//    private Double price;
//
//    @Column(nullable = true) // Allow null values
//    private String customerName;
//
//    @Column(nullable = true) // Allow null values
//    private String customerEmail;
//
//    @Column(nullable = true) // Allow null values
//    private String street;
//
//    @Column(nullable = true) // Allow null values
//    private String city;
//
//    @Column(nullable = true) // Allow null values
//    private String state;
//
//    @Column(nullable = true) // Allow null values
//    private String zipCode;
//
//    @Column(nullable = true) // Allow null values
//    private String country;
//
//    @Column(nullable = true) // Allow null values
//    private String contactNumber;
//
//    @Column(nullable = true) // Allow null values
//    private String paymentMethod;
//
//    @Column(nullable = true) // Allow null values
//    private String paymentStatus;
//
//    @Column(nullable = true) // Allow null values
//    private String orderStatus;
//
//    @CreationTimestamp
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column
//    private LocalDateTime updatedAt;
//}