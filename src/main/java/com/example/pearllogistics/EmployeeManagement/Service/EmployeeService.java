package com.example.pearllogistics.EmployeeManagement.Service;

import com.example.pearllogistics.EmployeeManagement.DTO.*;
import com.example.pearllogistics.EmployeeManagement.Entity.Employee;
import com.example.pearllogistics.EmployeeManagement.JWT.EmployeeJWT;
import com.example.pearllogistics.EmployeeManagement.Repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeJWT employeeJWT;

    public EmployeeResponse registerEmployee(EmployeeRegisterDTO registerDTO, MultipartFile image) throws IOException {

        if (employeeRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            return new EmployeeResponse( false, "Email already registered", null);
        }


        Employee employee = new Employee();
        if (image != null && !image.isEmpty()) {
            employee.setImage(image.getBytes());
        }
        employee.setName(registerDTO.getName());
        employee.setEmail(registerDTO.getEmail());
        employee.setCountry(registerDTO.getCountry());
        employee.setContactno(registerDTO.getContactno());
        employee.setDescription(registerDTO.getDescription());
        employee.setAddress(registerDTO.getAddress());
        employee.setRole(registerDTO.getRole());


        employee.setPassword(passwordEncoder.encode(registerDTO.getPassword()));


        Employee savedEmployee = employeeRepository.save(employee);


        Map<String, Object> claims = new HashMap<>();
        claims.put("id", savedEmployee.getId());
        claims.put("role", savedEmployee.getRole());
        String token = employeeJWT.generateToken(claims, savedEmployee.getEmail());

        return new EmployeeResponse(true, "Employee registered successfully", savedEmployee);
    }

    public EmployeeResponse loginEmployee(EmployeeLoginDTO loginDTO) {

        Optional<Employee> employeeOptional = employeeRepository.findByEmail(loginDTO.getEmail());

        if (employeeOptional.isEmpty()) {
            return new EmployeeResponse( false, "Invalid credentials", null);
        }

        Employee employee = employeeOptional.get();


        if (!passwordEncoder.matches(loginDTO.getPassword(), employee.getPassword())) {
            return new EmployeeResponse( false, "Invalid credentials", null);
        }


        if (!"Approved".equals(employee.getStatus())) {
            return new EmployeeResponse( false, "Account is pending approval", null);
        }


        Map<String, Object> claims = new HashMap<>();
        claims.put("id", employee.getId());
        claims.put("name", employee.getName());
        claims.put("role", employee.getRole());
        if (employee.getImage() != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(employee.getImage());
            claims.put("image", imageBase64);
        }
        String token = employeeJWT.generateToken(claims, employee.getEmail());

        return new EmployeeResponse(true, "Login successful", employee);
    }


    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public boolean deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public ResponseDTO approveEmployee(Long id, int salary ,String role ) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(id);

            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                employee.setStatus("Approved");
                employee.setSalary(salary);
                employee.setRole(role);

                employeeRepository.save(employee);

                return ResponseDTO.builder()
                        .status("success")
                        .message("Employee approved successfully")
                        .build();
            } else {
                return ResponseDTO.builder()
                        .status("error")
                        .message("Employee not found with ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .status("error")
                    .message("Failed to approve employee: " + e.getMessage())
                    .build();
        }
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }


    @Transactional
    public Employee updateEmployee(Long id, UpdateProfileRequest request) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            return null;
        }

        Employee employee = optionalEmployee.get();

        // Update only editable fields
        if (request.getName() != null) {
            employee.setName(request.getName());
        }

        if (request.getEmail() != null) {
            employee.setEmail(request.getEmail());
        }

        if (request.getCountry() != null) {
            employee.setCountry(request.getCountry());
        }

        if (request.getContactno() > 0) {
            employee.setContactno(request.getContactno());
        }

        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }

        if (request.getDescription() != null) {
            employee.setDescription(request.getDescription());
        }

        // Handle image update if provided
        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(request.getImageBase64());
                employee.setImage(imageBytes);
            } catch (IllegalArgumentException e) {
                // Invalid base64 string - log error
                System.err.println("Error decoding image: " + e.getMessage());
            }
        }

        // id, role, salary, djoined are not updated as required

        return employeeRepository.save(employee);
    }
}


