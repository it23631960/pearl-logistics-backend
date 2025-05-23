package com.example.pearllogistics.CategoryManagement.Repository;



import com.example.pearllogistics.CategoryManagement.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

