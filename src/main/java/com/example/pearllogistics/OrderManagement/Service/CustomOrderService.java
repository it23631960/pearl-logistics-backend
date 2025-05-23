package com.example.pearllogistics.OrderManagement.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pearllogistics.OrderManagement.DTO.CustomOrderDTO;
import com.example.pearllogistics.OrderManagement.Entity.CustomOrderEntity;
import com.example.pearllogistics.OrderManagement.Repository.CustomOrderRepository;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import com.example.pearllogistics.UserManagement.Repository.UserRepository;
import com.example.pearllogistics.UserManagement.Service.UserService;
import com.example.pearllogistics.Email.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOrderService {
    private final CustomOrderRepository customOrderRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final Cloudinary cloudinary;

    private static final Logger log = LoggerFactory.getLogger(CustomOrderService.class);

    @Transactional
    public CustomOrderDTO.CustomOrderResponse createCustomOrder(
            CustomOrderDTO.CustomOrderRequest request,
            MultipartFile[] images) throws IOException {
        UserEntity user;
        // If userId is provided, fetch the user
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElse(userService.getOrCreateGuestUser()); // Use guest user if the provided ID doesn't exist
        } else {
            // Always use guest user for non-logged in customers
            user = userService.getOrCreateGuestUser();
        }

        String customerName = request.getFirstName() + " " + request.getLastName();
        CustomOrderEntity customOrder = CustomOrderEntity.builder()
                .user(user) // This will never be null now
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .customerName(customerName)
                .customerEmail(request.getEmail())
                .contactNumber(request.getContactNumber())
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .productName(request.getName())
                .productDescription(request.getDescription())
                .fromCountry(request.getFromCountry())
                .productLink(request.getProductLink())
                .cashOnDelivery(request.getCashOnDelivery())
                .orderStatus("Pending")
                .paymentStatus(request.getCashOnDelivery() ? "Pending" : "Not Applicable")
                .paymentMethod(request.getCashOnDelivery() ? "Cash On Delivery" : "Not Applicable")
                .build();

        // Process images if provided
        if (images != null) {
            for (int i = 0; i < images.length && i < 4; i++) {
                if (images[i] != null && !images[i].isEmpty()) {
                    // Upload to Cloudinary instead of storing bytes
                    Map uploadResult = cloudinary.uploader().upload(
                            images[i].getBytes(),
                            ObjectUtils.asMap("resource_type", "auto")
                    );
                    String imageUrl = (String) uploadResult.get("secure_url");
                    String publicId = (String) uploadResult.get("public_id");

                    // Store URL instead of bytes
                    switch (i) {
                        case 0:
                            customOrder.setImage1Url(imageUrl);
                            customOrder.setImage1PublicId(publicId);
                            break;
                        case 1:
                            customOrder.setImage2Url(imageUrl);
                            customOrder.setImage2PublicId(publicId);
                            break;
                        case 2:
                            customOrder.setImage3Url(imageUrl);
                            customOrder.setImage3PublicId(publicId);
                            break;
                        case 3:
                            customOrder.setImage4Url(imageUrl);
                            customOrder.setImage4PublicId(publicId);
                            break;
                    }
                }
            }
        }

        CustomOrderEntity savedOrder = customOrderRepository.save(customOrder);

        // Send confirmation email to the customer
        try {
            emailService.sendCustomOrderConfirmationEmail(
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getContactNumber(),
                    request.getStreet(),
                    request.getCity(),
                    request.getState(),
                    request.getZipCode(),
                    request.getCountry(),
                    request.getName(),
                    request.getDescription(),
                    request.getFromCountry(),
                    request.getProductLink(),
                    request.getCashOnDelivery()
            );
        } catch (MessagingException e) {
            log.error("Failed to send order confirmation email: {}", e.getMessage());
            // We don't want the order to fail if the email fails, so we just log the error
        }

        return mapToCustomOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<CustomOrderDTO.CustomOrderResponse> getAllCustomOrders() {
        return customOrderRepository.findAll().stream()
                .map(this::mapToCustomOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomOrderDTO.CustomOrderResponse> getCustomOrdersByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return customOrderRepository.findByUser(user).stream()
                .map(this::mapToCustomOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomOrderDTO.CustomOrderResponse getCustomOrderById(Long orderId) {
        CustomOrderEntity order = customOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Custom order not found with ID: " + orderId));
        return mapToCustomOrderResponse(order);
    }

    @Transactional
    public CustomOrderDTO.CustomOrderResponse updateOrderStatus(Long orderId, CustomOrderDTO.StatusUpdateRequest request) {
        CustomOrderEntity order = customOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Custom order not found with ID: " + orderId));

        order.setOrderStatus(request.getOrderStatus());
        // If status is approved and payment is COD, update payment status
        if ("Approved".equalsIgnoreCase(request.getOrderStatus()) &&
                Boolean.TRUE.equals(order.getCashOnDelivery())) {
            order.setPaymentStatus("Awaiting Payment");
        }

        CustomOrderEntity updatedOrder = customOrderRepository.save(order);
        return mapToCustomOrderResponse(updatedOrder);
    }

    private CustomOrderDTO.CustomOrderResponse mapToCustomOrderResponse(CustomOrderEntity order) {
        CustomOrderDTO.CustomOrderResponse response = CustomOrderDTO.CustomOrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .customerName(order.getFirstName() + " " + order.getLastName())
                .customerEmail(order.getCustomerEmail())
                .contactNumber(order.getContactNumber())
                .street(order.getStreet())
                .city(order.getCity())
                .state(order.getState())
                .zipCode(order.getZipCode())
                .country(order.getCountry())
                .productName(order.getProductName())
                .productDescription(order.getProductDescription())
                .fromCountry(order.getFromCountry())
                .productLink(order.getProductLink())
                .cashOnDelivery(order.getCashOnDelivery())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .createdAt(order.getCreatedAt())
                .build();

        // Add image URLs to response
        if (order.getImage1Url() != null) {
            response.setImage1Url(order.getImage1Url());
        }
        if (order.getImage2Url() != null) {
            response.setImage2Url(order.getImage2Url());
        }
        if (order.getImage3Url() != null) {
            response.setImage3Url(order.getImage3Url());
        }
        if (order.getImage4Url() != null) {
            response.setImage4Url(order.getImage4Url());
        }

        return response;
    }

    @Transactional
    public void deleteCustomOrder(Long orderId) {
        CustomOrderEntity order = customOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Custom order not found with ID: " + orderId));

        // Delete images from Cloudinary
        try {
            if (order.getImage1PublicId() != null) {
                cloudinary.uploader().destroy(order.getImage1PublicId(), ObjectUtils.emptyMap());
            }
            if (order.getImage2PublicId() != null) {
                cloudinary.uploader().destroy(order.getImage2PublicId(), ObjectUtils.emptyMap());
            }
            if (order.getImage3PublicId() != null) {
                cloudinary.uploader().destroy(order.getImage3PublicId(), ObjectUtils.emptyMap());
            }
            if (order.getImage4PublicId() != null) {
                cloudinary.uploader().destroy(order.getImage4PublicId(), ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            log.error("Error deleting images from Cloudinary: {}", e.getMessage());
            // Continue with order deletion even if Cloudinary deletion fails
        }

        // Before deleting, send notification email to the customer
        try {
            // Get the necessary information for the email
            String customerEmail = order.getCustomerEmail();
            String customerName = order.getFirstName() + " " + order.getLastName();
            String orderIdStr = orderId.toString();
            String productName = order.getProductName() != null ? order.getProductName() : "Custom Product";

            // Send the email notification
            emailService.sendOrderDeletionNotificationEmail(
                    customerEmail,
                    customerName,
                    orderIdStr,
                    productName
            );
            log.info("Order deletion notification email sent to: {}", customerEmail);
        } catch (MessagingException e) {
            log.error("Failed to send order deletion notification email: {}", e.getMessage());
            // Continue with deletion even if email fails
        }

        // Proceed with deletion
        customOrderRepository.delete(order);
        log.info("Custom order with ID: {} has been deleted", orderId);
    }
}