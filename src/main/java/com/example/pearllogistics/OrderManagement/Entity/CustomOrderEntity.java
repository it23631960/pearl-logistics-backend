package com.example.pearllogistics.OrderManagement.Entity;

import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "custom_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = true)
    private String productDescription;

    @Column(nullable = true)
    private String productLink;

    @Column(nullable = false)
    private String fromCountry;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String street;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String zipCode;

    @Column(nullable = true)
    private String country;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String paymentStatus;

    @Column(nullable = false)
    private String orderStatus;

    @Column
    private Boolean cashOnDelivery;

    // Replace byte arrays with URLs and public IDs
    @Column(name = "image1_url")
    private String image1Url;

    @Column(name = "image1_public_id")
    private String image1PublicId;

    @Column(name = "image2_url")
    private String image2Url;

    @Column(name = "image2_public_id")
    private String image2PublicId;

    @Column(name = "image3_url")
    private String image3Url;

    @Column(name = "image3_public_id")
    private String image3PublicId;

    @Column(name = "image4_url")
    private String image4Url;

    @Column(name = "image4_public_id")
    private String image4PublicId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}