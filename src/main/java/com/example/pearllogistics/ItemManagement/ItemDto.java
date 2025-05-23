package com.example.pearllogistics.ItemManagement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long itemId;
    private byte[] image1;
    private byte[] image2;
    private byte[] image3;
    private byte[] image4;
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private Double price;
    private Boolean bestseller;
}