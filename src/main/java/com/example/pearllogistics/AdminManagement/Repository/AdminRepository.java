package com.example.pearllogistics.AdminManagement.Repository;

import com.example.pearllogistics.AdminManagement.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);
}
