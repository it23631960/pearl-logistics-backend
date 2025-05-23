package com.example.pearllogistics.UserManagement.Controller;
import com.example.pearllogistics.UserManagement.Dto.AuthRequest;
import com.example.pearllogistics.UserManagement.Dto.AuthResponse;
import com.example.pearllogistics.UserManagement.Dto.DeleteDTO;
import com.example.pearllogistics.UserManagement.Dto.UpdateDTO;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import com.example.pearllogistics.UserManagement.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        logger.info("Registration request received for email: {}", request.getEmail());


        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            logger.error("Registration failed: Email is required");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, false, "Email is required", null));
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            logger.error("Registration failed: Password is required");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, false, "Password is required", null));
        }

        if (request.getContactNo() == null || request.getContactNo().isEmpty()) {
            logger.error("Registration failed: Contact Number is required");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, false, "Contact Number is required", null));
        }


        AuthResponse response = userService.registerUser(request);

        if (response.isSuccess()) {
            logger.info("Registration successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } else {
            logger.error("Registration failed: {}", response.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());
        AuthResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "get-users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<AuthResponse> update(
            @RequestParam("user") String userJson,
            @RequestParam(value = "image", required = false) MultipartFile image ) throws IOException {

        System.out.println("Received user JSON: " + userJson);

        try {
            UpdateDTO userRequest = objectMapper.readValue(userJson, UpdateDTO.class);

            return ResponseEntity.ok(userService.updateUser(userRequest, image));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new AuthResponse(null, false, "Error in controller", null));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteDTO> deleteItem(@PathVariable Long id) {
        try {
            boolean deleted = userService.deleteItem(id);
            if (deleted) {
                return ResponseEntity.ok(
                        DeleteDTO.builder()
                                .status("success")
                                .message("Item deleted successfully")
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        DeleteDTO.builder()
                                .status("error")
                                .message("Item not found")
                                .build()
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    DeleteDTO.builder()
                            .status("error")
                            .message("Error deleting item: " + e.getMessage())
                            .build()
            );
        }
    }

}