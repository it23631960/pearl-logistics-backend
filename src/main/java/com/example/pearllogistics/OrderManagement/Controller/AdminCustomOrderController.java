package com.example.pearllogistics.OrderManagement.Controller;

import com.example.pearllogistics.ItemManagement.ResponseDTO;
import com.example.pearllogistics.OrderManagement.DTO.CustomOrderDTO;
import com.example.pearllogistics.OrderManagement.Service.CustomOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/custom-orders")
@RequiredArgsConstructor
public class AdminCustomOrderController {
    
    private final CustomOrderService customOrderService;
    
    @GetMapping
    public ResponseEntity<List<CustomOrderDTO.CustomOrderResponse>> getAllCustomOrders() {
        return ResponseEntity.ok(customOrderService.getAllCustomOrders());
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<CustomOrderDTO.CustomOrderResponse> getCustomOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(customOrderService.getCustomOrderById(orderId));
    }
    
    @PutMapping("/{orderId}/status")
    public ResponseEntity<CustomOrderDTO.CustomOrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody CustomOrderDTO.StatusUpdateRequest request) {
        return ResponseEntity.ok(customOrderService.updateOrderStatus(orderId, request));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteCustomOrder(@PathVariable Long orderId) {
        customOrderService.deleteCustomOrder(orderId);
        return ResponseEntity.noContent().build();
    }


}