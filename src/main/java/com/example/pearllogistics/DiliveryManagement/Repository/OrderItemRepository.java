package com.example.pearllogistics.DiliveryManagement.Repository;



import com.example.pearllogistics.DiliveryManagement.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}