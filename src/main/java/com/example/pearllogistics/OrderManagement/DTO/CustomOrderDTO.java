package com.example.pearllogistics.OrderManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CustomOrderDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomOrderRequest {
        private Long userId;

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
        private String name;
        private String description;
        private String fromCountry;
        private String productLink;
        private Boolean cashOnDelivery;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomOrderResponse {
        private Long id;
        private Long userId;

        // Customer Information
        private String customerName;
        private String customerEmail;
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

        // Image URLs
        private String image1Url;
        private String image2Url;
        private String image3Url;
        private String image4Url;

        // Order Status
        private String orderStatus;
        private String paymentStatus;
        private String paymentMethod;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {
        private String orderStatus;
    }
}