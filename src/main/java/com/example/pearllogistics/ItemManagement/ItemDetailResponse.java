package com.example.pearllogistics.ItemManagement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailResponse {
    private boolean success;
    private String message;
    private Item item;
}
