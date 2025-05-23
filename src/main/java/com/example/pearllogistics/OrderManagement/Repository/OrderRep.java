//package com.example.pearllogistics.OrderManagement.Repository;
//
//import com.example.pearllogistics.OrderManagement.Entity.OrderEntity;
//import com.example.pearllogistics.UserManagement.Entity.UserEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface OrderRep extends JpaRepository<OrderEntity, Long> {
//    List<OrderEntity> findByUser(UserEntity user);
//    Optional<OrderEntity> findById(Long id);
//}