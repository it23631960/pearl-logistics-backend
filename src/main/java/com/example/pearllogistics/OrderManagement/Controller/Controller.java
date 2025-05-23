//package com.example.pearllogistics.OrderManagement.Controller;
//
//import com.example.pearllogistics.OrderManagement.DTO.OrderDTO;
//import com.example.pearllogistics.OrderManagement.Service.OrderSer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/orders")
//@RequiredArgsConstructor
//public class Controller {
//
//    private final OrderSer orderService;
//
//    @GetMapping
//    public ResponseEntity<List<OrderDTO.OrderResponse>> getAllOrders() {
//        return ResponseEntity.ok(orderService.getAllOrders());
//    }
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderDTO.OrderResponse> getOrderById(@PathVariable Long orderId) {
//        return ResponseEntity.ok(orderService.getOrderById(orderId));
//    }
//
//    @PutMapping("/{orderId}/status")
//    public ResponseEntity<OrderDTO.OrderResponse> updateOrderStatus(
//            @PathVariable Long orderId,
//            @RequestBody OrderDTO.StatusUpdateRequest statusRequest) {
//        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, statusRequest));
//    }
//
//    @DeleteMapping("/{orderId}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
//        orderService.deleteOrder(orderId);
//        return ResponseEntity.noContent().build();
//    }
//}