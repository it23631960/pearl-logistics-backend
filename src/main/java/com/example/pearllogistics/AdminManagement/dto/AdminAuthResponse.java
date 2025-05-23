package com.example.pearllogistics.AdminManagement.dto;

import com.example.pearllogistics.AdminManagement.Entity.Admin;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminAuthResponse {
    private boolean success;
    private String message;
    private Admin admin;
}
