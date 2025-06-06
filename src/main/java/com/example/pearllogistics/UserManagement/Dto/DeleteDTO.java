package com.example.pearllogistics.UserManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DeleteDTO {
    private String status;
    private String message;
}
