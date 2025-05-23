package com.example.pearllogistics.TicketManagement.Repository;

import com.example.pearllogistics.TicketManagement.Entity.TicketEntity;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByUser(UserEntity user);
    List<TicketEntity> findByReplied(boolean replied);
    List<TicketEntity> findByUserAndReplied(UserEntity user, boolean replied);
}