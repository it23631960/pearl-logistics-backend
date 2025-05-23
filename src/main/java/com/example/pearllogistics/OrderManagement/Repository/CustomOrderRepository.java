package com.example.pearllogistics.OrderManagement.Repository;

import com.example.pearllogistics.OrderManagement.Entity.CustomOrderEntity;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomOrderRepository extends JpaRepository<CustomOrderEntity, Long> {
    List<CustomOrderEntity> findByUser(UserEntity user);
    List<CustomOrderEntity> findByOrderStatus(String status);
    List<CustomOrderEntity> findByUserAndOrderStatus(UserEntity user, String status);
}