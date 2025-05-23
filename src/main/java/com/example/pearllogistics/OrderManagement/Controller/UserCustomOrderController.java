package com.example.pearllogistics.OrderManagement.Controller;

import com.example.pearllogistics.OrderManagement.DTO.CustomOrderDTO;
import com.example.pearllogistics.OrderManagement.Service.CustomOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user/custom-orders")
@RequiredArgsConstructor
public class UserCustomOrderController {
    
    private final CustomOrderService customOrderService;
    private final ObjectMapper objectMapper;
    
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CustomOrderDTO.CustomOrderResponse> createCustomOrder(
            @RequestParam("order") String orderJson,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {
        
        try {
            CustomOrderDTO.CustomOrderRequest request = objectMapper.readValue(orderJson, 
                    CustomOrderDTO.CustomOrderRequest.class);
            
            CustomOrderDTO.CustomOrderResponse response = customOrderService.createCustomOrder(request, images);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CustomOrderDTO.CustomOrderResponse>> getUserCustomOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(customOrderService.getCustomOrdersByUserId(userId));
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<CustomOrderDTO.CustomOrderResponse> getCustomOrderDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(customOrderService.getCustomOrderById(orderId));
    }
}