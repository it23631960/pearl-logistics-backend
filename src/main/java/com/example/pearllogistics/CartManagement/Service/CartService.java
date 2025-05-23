package com.example.pearllogistics.CartManagement.Service;

import com.example.pearllogistics.CartManagement.Dto.CartDto;
import com.example.pearllogistics.CartManagement.Entity.*;
import com.example.pearllogistics.CartManagement.Repository.CartRepository;
import com.example.pearllogistics.CartManagement.Repository.LineItemRepository;
import com.example.pearllogistics.ItemManagement.Item;
import com.example.pearllogistics.ItemManagement.ItemRepository;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import com.example.pearllogistics.UserManagement.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final LineItemRepository lineItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public CartDto.CartResponse addToCart(Long userId, CartDto.CartRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        LineItem lineItem = lineItemRepository.findByCartAndItem(cart, item)
                .orElse(LineItem.builder().cart(cart).item(item).build());

        if (lineItem.getQuantity() == null) {
            lineItem.setQuantity(request.getQuantity());
        } else {
            lineItem.setQuantity(lineItem.getQuantity() + request.getQuantity());
        }

        lineItem.setTotalPrice(item.getPrice() * lineItem.getQuantity());
        lineItemRepository.save(lineItem);

        return mapToCartResponse(lineItem);
    }

    @Transactional(readOnly = true)
    public List<CartDto.CartResponse> getUserCart(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        return lineItemRepository.findByCart(cart)
                .stream()
                .map(this::mapToCartResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartDto.CartResponse updateCartItem(Long userId, CartDto.CartUpdateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        LineItem lineItem = lineItemRepository.findByCartAndItem(cart, item)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        lineItem.setQuantity(request.getQuantity());
        lineItem.setTotalPrice(lineItem.getItem().getPrice() * request.getQuantity());

        lineItemRepository.save(lineItem);
        return mapToCartResponse(lineItem);
    }

    @Transactional
    public void removeCartItem(Long userId, Long itemId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        lineItemRepository.deleteByCartAndItem(cart, item);
    }

    private CartDto.CartResponse mapToCartResponse(LineItem lineItem) {
        return CartDto.CartResponse.builder()
                .id(lineItem.getCart().getId())
                .userId(lineItem.getCart().getUser().getId())
                .itemId(lineItem.getItem().getItemId())
                .itemName(lineItem.getItem().getName())
                .quantity(lineItem.getQuantity())
                .price(lineItem.getItem().getPrice())
                .totalPrice(lineItem.getTotalPrice())
                .build();
    }

    @Transactional
    public void clearUserCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
