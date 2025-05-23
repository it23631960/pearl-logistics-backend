package com.example.pearllogistics.DiliveryManagement.Controller;

import com.example.pearllogistics.DiliveryManagement.DTO.OrderDTO.*;
import com.example.pearllogistics.DiliveryManagement.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order
     * @param orderRequest Order request data containing user ID, items, payment details
     * @return Complete order response with order details, user information, and item details
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Get all orders with complete user and item details
     * @return List of orders with full details
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get a specific order by ID with complete user and item details
     * @param orderId Order ID to retrieve
     * @return Order with full details
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Get all orders for a specific user with complete user and item details
     * @param userId User ID to retrieve orders for
     * @return List of orders for the user with full details
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Update order status
     * @param orderId Order ID to update
     * @param request New order status
     * @return Updated order with full details
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Update payment status
     * @param orderId Order ID to update
     * @param request New payment status
     * @return Updated order with full details
     */
    @PutMapping("/{orderId}/payment")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestBody PaymentStatusUpdateRequest request) {
        OrderResponse updatedOrder = orderService.updatePaymentStatus(orderId, request);
        return ResponseEntity.ok(updatedOrder);
    }
}