package com.example.pearllogistics.EmployeeManagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRegisterDTO {
    private byte[] image;
    private String name;
    private String email;
    private String country;
    private int contactno;
    private String description;
    private String address;
    private String role ;
    private String password;
}
