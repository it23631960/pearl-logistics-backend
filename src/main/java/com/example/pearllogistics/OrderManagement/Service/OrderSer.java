//package com.example.pearllogistics.OrderManagement.Service;
//
//import com.example.pearllogistics.OrderManagement.DTO.OrderDTO;
//import com.example.pearllogistics.OrderManagement.Entity.OrderEntity;
//import com.example.pearllogistics.OrderManagement.Repository.OrderRep;
//import com.example.pearllogistics.UserManagement.Entity.UserEntity;
//import com.example.pearllogistics.UserManagement.Repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class OrderSer {
//
//    private final OrderRep orderRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public OrderDTO.OrderResponse createOrder(OrderDTO.OrderRequest request) {
//        UserEntity user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));
//
//        OrderEntity order = OrderEntity.builder()
//                .user(user)
//                .productName(request.getProductName())
//                .price(request.getPrice())
//                .customerName(request.getCustomerName())
//                .customerEmail(request.getCustomerEmail())
//                .street(request.getStreet())
//                .city(request.getCity())
//                .state(request.getState())
//                .zipCode(request.getZipCode())
//                .country(request.getCountry())
//                .contactNumber(request.getContactNumber())
//                .paymentMethod(request.getPaymentMethod())
//                .paymentStatus(request.getPaymentStatus())
//                .orderStatus("Processing")  // Default status
//                .build();
//
//        OrderEntity savedOrder = orderRepository.save(order);
//        return mapToOrderResponse(savedOrder);
//    }
//
//    @Transactional(readOnly = true)
//    public List<OrderDTO.OrderResponse> getAllOrders() {
//        return orderRepository.findAll().stream()
//                .map(this::mapToOrderResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<OrderDTO.OrderResponse> getOrdersByUser(Long userId) {
//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//        return orderRepository.findByUser(user).stream()
//                .map(this::mapToOrderResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public OrderDTO.OrderResponse getOrderById(Long orderId) {
//        OrderEntity order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
//        return mapToOrderResponse(order);
//    }
//
//    @Transactional
//    public OrderDTO.OrderResponse updateOrderStatus(Long orderId, OrderDTO.StatusUpdateRequest statusRequest) {
//        OrderEntity order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
//
//        order.setOrderStatus(statusRequest.getOrderStatus());
//        order.setUpdatedAt(LocalDateTime.now());
//
//        OrderEntity updatedOrder = orderRepository.save(order);
//        return mapToOrderResponse(updatedOrder);
//    }
//
//    private OrderDTO.OrderResponse mapToOrderResponse(OrderEntity order) {
//        return OrderDTO.OrderResponse.builder()
//                .id(order.getId())
//                .userId(order.getUser().getId())
//                .productName(order.getProductName())
//                .price(order.getPrice())
//                .customerName(order.getCustomerName())
//                .customerEmail(order.getCustomerEmail())
//                .street(order.getStreet())
//                .city(order.getCity())
//                .state(order.getState())
//                .zipCode(order.getZipCode())
//                .country(order.getCountry())
//                .contactNumber(order.getContactNumber())
//                .paymentMethod(order.getPaymentMethod())
//                .paymentStatus(order.getPaymentStatus())
//                .orderStatus(order.getOrderStatus())
//                .createdAt(order.getCreatedAt())
//                .updatedAt(order.getUpdatedAt())
//                .build();
//    }
//
//    @Transactional
//    public void deleteOrder(Long orderId) {
//        OrderEntity order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
//
//        orderRepository.delete(order);
//    }
//}