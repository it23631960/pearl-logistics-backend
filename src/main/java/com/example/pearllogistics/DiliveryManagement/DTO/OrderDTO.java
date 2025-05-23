package com.example.pearllogistics.DiliveryManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderRequest {
        private Long userId;
        private String paymentMethod;
        private BigDecimal itemsTotal;
        private BigDecimal shippingCharges;
        private BigDecimal otherCharges;
        private BigDecimal totalAmount;
        private List<OrderItemRequest> items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal totalPrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderResponse {
        private Long id;
        private Long userId;
        private UserDTO user;  // Added complete user information
        private String orderStatus;
        private LocalDateTime createdAt;
        private String paymentMethod;
        private String paymentStatus;
        private BigDecimal itemsTotal;
        private BigDecimal shippingCharges;
        private BigDecimal otherCharges;
        private BigDecimal totalAmount;
        private List<OrderItemResponse> items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal totalPrice;
        private ItemDTO item;  // Added complete item information
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String email;
        private Long contactNo;
        private String firstName;
        private String lastName;
        private String street;
        private String city;
        private String state;
        private String zipcode;
        private String country;
        private String role;
        private LocalDateTime dJoined;
        // Image binary data excluded for security and performance
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDTO {
        private Long itemId;
        private String name;
        private String description;
        private String category;
        private Integer quantity;
        private Double price;
        private Boolean bestseller;
        // Image binary data excluded for performance
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderStatusUpdateRequest {
        private String orderStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentStatusUpdateRequest {
        private String paymentStatus;
    }
}