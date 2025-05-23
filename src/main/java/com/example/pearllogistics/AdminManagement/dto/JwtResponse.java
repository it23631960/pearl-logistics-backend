package com.example.pearllogistics.AdminManagement.dto;
import com.example.pearllogistics.AdminManagement.Entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String message;
    private Admin admin;
}
