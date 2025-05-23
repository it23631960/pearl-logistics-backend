package com.example.pearllogistics.ItemManagement;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemAuthResponse {
    private boolean success;
    private String message;
    private Item item;
}

