//package com.example.pearllogistics.OrderManagement.Controller;
//
//import com.example.pearllogistics.OrderManagement.DTO.OrderDTO;
//import com.example.pearllogistics.OrderManagement.Service.OrderSer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/user/orders")
//@RequiredArgsConstructor
//public class UserOrderController {
//
//    private final OrderSer orderService;
//
//    @PostMapping
//    public ResponseEntity<OrderDTO.OrderResponse> createOrder(@RequestBody OrderDTO.OrderRequest request) {
//        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<OrderDTO.OrderResponse>> getUserOrders(@PathVariable Long userId) {
//        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
//    }
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderDTO.OrderResponse> getOrderDetails(@PathVariable Long orderId) {
//        return ResponseEntity.ok(orderService.getOrderById(orderId));
//    }
//}