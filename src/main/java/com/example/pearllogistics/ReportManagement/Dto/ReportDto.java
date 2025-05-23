package com.example.pearllogistics.ReportManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReportDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportRequest {
        private Long userId;
        private String reportName;
        private String reportType;
        private byte[] data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportResponse {
        private Long id;
        private Long userId;
        private String userName;
        private String reportName;
        private String reportType;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportDetailResponse {
        private Long id;
        private Long userId;
        private String userName;
        private String reportName;
        private String reportType;
        private byte[] data;
        private LocalDateTime createdAt;
    }
}