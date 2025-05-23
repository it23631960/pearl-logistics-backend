package com.example.pearllogistics.EmployeeManagement.DTO;

public class  UpdateProfileRequest {
    private String name;
    private String email;
    private String country;
    private int contactno;
    private String description;
    private String address;
    private String imageBase64;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getContactno() { return contactno; }
    public void setContactno(int contactno) { this.contactno = contactno; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
