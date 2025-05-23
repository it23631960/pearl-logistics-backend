package com.example.pearllogistics.UserManagement.Dto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateDTO {

    private Long id;
    private String email;
    private Long contactNo;
    private byte[] image;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
