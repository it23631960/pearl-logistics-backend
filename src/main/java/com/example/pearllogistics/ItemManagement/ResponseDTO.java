package com.example.pearllogistics.ItemManagement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
    private String status;
    private String message;
    private Object data;
}