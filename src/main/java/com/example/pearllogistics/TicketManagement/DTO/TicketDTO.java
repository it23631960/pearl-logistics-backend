package com.example.pearllogistics.TicketManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TicketDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketRequest {
        private Long userId;
        private String name;
        private String subject;
        private String email;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketResponse {
        private Long id;
        private Long userId;
        private String name;
        private String subject;
        private String email;
        private String description;
        private String reply;
        private boolean replied;
        private LocalDateTime createdAt;
        private LocalDateTime repliedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdminReplyRequest {
        private String reply;
    }
}