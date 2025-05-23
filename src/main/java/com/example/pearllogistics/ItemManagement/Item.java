package com.example.pearllogistics.ItemManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Lob
    @Column(name = "image1")
    private byte[] image1;

    @Lob
    @Column(name = "image2")
    private byte[] image2;

    @Lob
    @Column(name = "image3")
    private byte[] image3;

    @Lob
    @Column(name = "image4")
    private byte[] image4;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column
    private String category;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(nullable = false)
    private Double price = 0.0;

    @Column
    private Boolean bestseller = false;
}