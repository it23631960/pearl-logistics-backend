package com.example.pearllogistics.CartManagement.Repository;

import com.example.pearllogistics.CartManagement.Entity.Cart;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(UserEntity user);
    @Transactional
    void deleteByUserId(Long userId);
}
