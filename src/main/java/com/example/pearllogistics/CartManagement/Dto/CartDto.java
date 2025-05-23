// Update your CartDto file to modify the CartUpdateRequest class
package com.example.pearllogistics.CartManagement.Dto;

import lombok.*;

public class CartDto {

    // Your existing CartRequest class
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartRequest {
        private Long itemId;
        private Integer quantity;
    }

    // Updated CartUpdateRequest class to match frontend data
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartUpdateRequest {
        private Long itemId;  // Changed from cartId to itemId
        private Integer quantity;
    }

    // Your existing CartResponse class
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartResponse {
        private Long id;
        private Long userId;
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private Double price;
        private Double totalPrice;
    }
}