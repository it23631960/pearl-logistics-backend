package com.example.pearllogistics.UserManagement.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String email;
    private String password;
    private String contactNo;
}
