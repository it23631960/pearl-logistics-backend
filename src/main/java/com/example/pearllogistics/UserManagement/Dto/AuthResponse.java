package com.example.pearllogistics.UserManagement.Dto;

import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private boolean success;
    private String message;
    private UserEntity user;

}
