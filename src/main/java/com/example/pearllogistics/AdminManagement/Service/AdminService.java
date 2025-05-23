package com.example.pearllogistics.AdminManagement.Service;

import com.example.pearllogistics.AdminManagement.Entity.Admin;
import com.example.pearllogistics.AdminManagement.Repository.AdminRepository;
import com.example.pearllogistics.AdminManagement.dto.AdminAuthRequest;
import com.example.pearllogistics.AdminManagement.dto.AdminAuthRequestLogin;
import com.example.pearllogistics.AdminManagement.dto.AdminAuthResponse;
import com.example.pearllogistics.UserManagement.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;

    public AdminService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AdminRepository adminRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
    }


    public AdminAuthResponse loginAdmin(AdminAuthRequestLogin request) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(request.getEmail());

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();


            if (passwordEncoder.matches(request.getPassword(), admin.getPassword())) {

                Map<String, Object> claims = new HashMap<>();
                claims.put("email", admin.getEmail());
                claims.put("name", admin.getName());


                if (admin.getImage() != null) {
                    String imageBase64 = Base64.getEncoder().encodeToString(admin.getImage());
                    claims.put("image", imageBase64);
                }

                String token = jwtUtil.generateToken(claims, admin.getEmail());

                return new AdminAuthResponse(true, "Login successful", admin);
            }
        }

        return new AdminAuthResponse(false, "Invalid email or password", null);
    }


    public AdminAuthResponse registerAdmin(AdminAuthRequest request, MultipartFile image) {
        try {

            if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
                return new AdminAuthResponse(false, "Email is already registered", null);
            }


            Admin admin = new Admin();
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setName(request.getName());


            if (image != null && !image.isEmpty()) {
                admin.setImage(image.getBytes());
            }


            adminRepository.save(admin);


            Map<String, Object> claims = new HashMap<>();
            claims.put("email", admin.getEmail());
            claims.put("name", admin.getName());


            if (admin.getImage() != null) {
                String imageBase64 = Base64.getEncoder().encodeToString(admin.getImage());
                claims.put("image", imageBase64);
            }

            String token = jwtUtil.generateToken(claims, admin.getEmail());


            return new AdminAuthResponse( true,"Registration successful", admin);
        } catch (IOException e) {
            return new AdminAuthResponse(false, "Error processing image: " + e.getMessage(), null);
        } catch (Exception e) {
            return new AdminAuthResponse(false, "Error during registration: " + e.getMessage(), null);
        }
    }
}
