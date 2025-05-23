package com.example.pearllogistics.AdminManagement.Controller;

import com.example.pearllogistics.AdminManagement.Service.AdminService;
import com.example.pearllogistics.AdminManagement.dto.AdminAuthRequest;
import com.example.pearllogistics.AdminManagement.dto.AdminAuthRequestLogin;
import com.example.pearllogistics.AdminManagement.dto.AdminAuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:8080",
    "https://*.railway.app",
    "https://pearl-logistics-frontend.vercel.app"
}, allowCredentials = "true",
        allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<AdminAuthResponse> register(
            @RequestPart("admin") String adminJson,
            @RequestPart("image") MultipartFile image) {
        ObjectMapper objectMapper = new ObjectMapper();
        AdminAuthRequest request;
        try {
            request = objectMapper.readValue(adminJson, AdminAuthRequest.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new AdminAuthResponse(false, "Invalid admin JSON format", null));
        }
        AdminAuthResponse response = adminService.registerAdmin(request, image);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:8080",
        "https://*.railway.app",
        "https://pearl-logistics-frontend.vercel.app"
    })
    public ResponseEntity<AdminAuthResponse> login(@RequestBody AdminAuthRequestLogin request) {
        AdminAuthResponse response = adminService.loginAdmin(request);
        return ResponseEntity.ok(response);
    }
}