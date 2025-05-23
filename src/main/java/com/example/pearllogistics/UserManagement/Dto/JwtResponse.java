package com.example.pearllogistics.UserManagement.Dto;


import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private UserEntity user;
}