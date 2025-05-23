package com.example.pearllogistics.OrderManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class OrderDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderRequest {
        private Long userId;
        private String productName;
        private Double price;
        private String customerName;
        private String customerEmail;
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
        private String contactNumber;
        private String paymentMethod;
        private String paymentStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderResponse {
        private Long id;
        private Long userId;
        private String productName;
        private Double price;
        private String customerName;
        private String customerEmail;
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
        private String contactNumber;
        private String paymentMethod;
        private String paymentStatus;
        private String orderStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusUpdateRequest {
        private String orderStatus;
    }
}