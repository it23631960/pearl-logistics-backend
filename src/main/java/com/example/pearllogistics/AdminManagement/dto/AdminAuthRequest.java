package com.example.pearllogistics.AdminManagement.dto;

import lombok.Getter;
import lombok.Setter;


    @Getter
    @Setter
    public class AdminAuthRequest {
        private String name;
        private String email;
        private String password;
    }


