package com.example.pearllogistics.DiliveryManagement.Service;

import com.example.pearllogistics.DiliveryManagement.DTO.OrderDTO.*;
import com.example.pearllogistics.DiliveryManagement.Entity.Order;
import com.example.pearllogistics.DiliveryManagement.Entity.OrderItem;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import com.example.pearllogistics.ItemManagement.Item;
import com.example.pearllogistics.DiliveryManagement.Repository.OrderRepository;
import com.example.pearllogistics.UserManagement.Repository.UserRepository;
import com.example.pearllogistics.ItemManagement.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Find the user
        UserEntity user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + orderRequest.getUserId()));

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setItemsTotal(orderRequest.getItemsTotal());
        order.setShippingCharges(orderRequest.getShippingCharges());
        order.setOtherCharges(orderRequest.getOtherCharges());
        order.setTotalAmount(orderRequest.getTotalAmount());

        // Set payment status based on payment method
        if ("COD".equalsIgnoreCase(orderRequest.getPaymentMethod())) {
            order.setPaymentStatus("UNPAID");
        } else {
            order.setPaymentStatus("PAID");
        }

        // Add order items
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            // Verify the item exists
            Item item = itemRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + itemRequest.getItemId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(item.getItemId());
            orderItem.setItemName(item.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(itemRequest.getPrice());
            orderItem.setTotalPrice(itemRequest.getTotalPrice());
            order.addOrderItem(orderItem);
        }

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Return order response with complete data
        return mapToOrderResponseWithDetails(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponseWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToOrderResponseWithDetails)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        return mapToOrderResponseWithDetails(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        order.setOrderStatus(request.getOrderStatus());
        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponseWithDetails(updatedOrder);
    }

    @Transactional
    public OrderResponse updatePaymentStatus(Long orderId, PaymentStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        order.setPaymentStatus(request.getPaymentStatus());
        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponseWithDetails(updatedOrder);
    }

    // Helper method to map Order entity to OrderResponse DTO with complete user and item details
    private OrderResponse mapToOrderResponseWithDetails(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());

        // Set basic order information
        response.setOrderStatus(order.getOrderStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setItemsTotal(order.getItemsTotal());
        response.setShippingCharges(order.getShippingCharges());
        response.setOtherCharges(order.getOtherCharges());
        response.setTotalAmount(order.getTotalAmount());

        // Set user details
        UserEntity user = order.getUser();
        response.setUserId(user.getId());
        UserDTO userDTO = mapToUserDTO(user);
        response.setUser(userDTO);

        // Map and set order items with item details
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(this::mapToOrderItemResponseWithDetails)
                .collect(Collectors.toList());
        response.setItems(items);

        return response;
    }

    // Helper method to map UserEntity to UserDTO
    private UserDTO mapToUserDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setContactNo(user.getContactNo());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setStreet(user.getStreet());
        userDTO.setCity(user.getCity());
        userDTO.setState(user.getState());
        userDTO.setZipcode(user.getZipcode());
        userDTO.setCountry(user.getCountry());
        userDTO.setRole(user.getRole());
        userDTO.setDJoined(user.getDJoined());
        // Don't include password and image binary data for security reasons
        return userDTO;
    }

    // Helper method to map OrderItem entity to OrderItemResponse DTO with item details
    private OrderItemResponse mapToOrderItemResponseWithDetails(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(orderItem.getId());
        response.setItemId(orderItem.getItemId());
        response.setItemName(orderItem.getItemName());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        response.setTotalPrice(orderItem.getTotalPrice());

        // Fetch and set item details
        try {
            Item item = itemRepository.findById(orderItem.getItemId())
                    .orElse(null);
            if (item != null) {
                ItemDTO itemDTO = mapToItemDTO(item);
                response.setItem(itemDTO);
            }
        } catch (Exception e) {
            // Log the exception and continue - don't fail the whole response if one item can't be found
            System.err.println("Error fetching item details for item ID: " + orderItem.getItemId() + " - " + e.getMessage());
        }

        return response;
    }

    // Helper method to map Item entity to ItemDTO
    private ItemDTO mapToItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(item.getItemId());
        itemDTO.setName(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setCategory(item.getCategory());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setBestseller(item.getBestseller());
        // Don't include image binary data for performance reasons
        return itemDTO;
    }
}