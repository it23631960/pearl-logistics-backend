package com.example.pearllogistics.OrderManagement.Entity;

import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class is now a data class for transferring custom order data.
 * It's no longer an entity to avoid conflicts with CustomOrderEntity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomOrder {
    private Long id;
    private UserEntity user;

    // Customer Information
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;

    // Shipping Address
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Product Details
    private String productName;
    private String productDescription;
    private String fromCountry;
    private String productLink;
    private Boolean cashOnDelivery;

    // Order Status
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;

    // Image URLs instead of byte arrays
    private String image1Url;
    private String image2Url;
    private String image3Url;
    private String image4Url;

    // Timestamps
    private LocalDateTime createdAt;
}