package com.example.pearllogistics.ItemManagement;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemAuthRequest {
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private Boolean bestseller;
}
