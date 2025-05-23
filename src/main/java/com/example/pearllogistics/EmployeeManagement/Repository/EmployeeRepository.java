package com.example.pearllogistics.EmployeeManagement.Repository;

import com.example.pearllogistics.EmployeeManagement.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);


    List<Employee> findByRole(String role);


    List<Employee> findByStatus(String status);


    List<Employee> findByCountry(String country);


    List<Employee> findBySalaryGreaterThan(int salary);


    List<Employee> findBySalaryLessThan(int salary);


    List<Employee> findByNameContainingIgnoreCase(String name);


    boolean existsByEmail(String email);


    long countByStatus(String status);


    long countByRole(String role);
}