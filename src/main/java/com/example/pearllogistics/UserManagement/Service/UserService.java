package com.example.pearllogistics.UserManagement.Service;

import com.example.pearllogistics.Email.Service.EmailService;
import com.example.pearllogistics.Email.Service.EmailService;
import com.example.pearllogistics.ItemManagement.Item;
import com.example.pearllogistics.ItemManagement.ItemAuthResponse;
import com.example.pearllogistics.UserManagement.Dto.AuthRequest;
import com.example.pearllogistics.UserManagement.Dto.AuthResponse;
import com.example.pearllogistics.UserManagement.Dto.UpdateDTO;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import com.example.pearllogistics.UserManagement.Repository.UserRepository;
import com.example.pearllogistics.UserManagement.Security.JwtUtil;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String GUEST_USER_EMAIL = "guest@pearllogistics.com";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    public AuthResponse registerUser(AuthRequest request) {
        try {
            logger.info("Attempting to register user with email: {}", request.getEmail());
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.warn("Email already registered: {}", request.getEmail());
                return new AuthResponse(null, false, "Email is already registered", null);
            }


            UserEntity user = new UserEntity();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));


            try {
                user.setContactNo(Long.parseLong(request.getContactNo()));
            } catch (NumberFormatException e) {
                logger.error("Invalid contact number format: {}", request.getContactNo());
                return new AuthResponse(null, false, "Invalid contact number format", null);
            }


            user.setFirstName(null);
            user.setLastName(null);
            user.setStreet(null);
            user.setCity(null);
            user.setState(null);
            user.setZipcode(null);
            user.setCountry(null);
            user.setImage(null);


            userRepository.save(user);

            if (emailEnabled) {
                try {
                    emailService.sendSignedInEmail(
                            user.getEmail()
                    );
                } catch (MessagingException e) {

                    System.err.println("Failed to send email notification: " + e.getMessage());

                }
            }
            logger.info("User registered successfully: {}", request.getEmail());


            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());
            claims.put("contactNo", user.getContactNo());
            String token = jwtUtil.generateToken(claims, user.getEmail());

            return new AuthResponse(token, true, "Registration successful", user);
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return new AuthResponse(null, false, "Error during registration: " + e.getMessage(), null);
        }
    }

    public AuthResponse loginUser(AuthRequest request) {
        try {
            logger.info("Login attempt for email: {}", request.getEmail());

            Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();


                if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {

                    Map<String, Object> claims = new HashMap<>();
                    claims.put("email", user.getEmail());
                    claims.put("contactNo", user.getContactNo());
                    String token = jwtUtil.generateToken(claims, user.getEmail());

                    logger.info("User logged in successfully: {}", request.getEmail());
                    return new AuthResponse(token, true, "Login successful", user);
                }
            }

            logger.warn("Login failed for email: {}", request.getEmail());
            return new AuthResponse(null, false, "Invalid email or password", null);
        } catch (Exception e) {
            logger.error("Error during login", e);
            return new AuthResponse(null, false, "Login error: " + e.getMessage(), null);
        }
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public AuthResponse updateUser(UpdateDTO userRequest, MultipartFile image) {
        try {

            Optional<UserEntity> existingUserOptional = userRepository.findById(userRequest.getId());

            if (!existingUserOptional.isPresent()) {
                return new AuthResponse(null, false, "User not found", null);
            }

            UserEntity existingUser = existingUserOptional.get();


            if (userRequest.getEmail() != null) {
                existingUser.setEmail(userRequest.getEmail());
            }

            if (userRequest.getContactNo() != null) {
                existingUser.setContactNo(userRequest.getContactNo());
            }

            if (userRequest.getFirstName() != null) {
                existingUser.setFirstName(userRequest.getFirstName());
            }

            if (userRequest.getLastName() != null) {
                existingUser.setLastName(userRequest.getLastName());
            }

            if (userRequest.getStreet() != null) {
                existingUser.setStreet(userRequest.getStreet());
            }

            if (userRequest.getCity() != null) {
                existingUser.setCity(userRequest.getCity());
            }

            if (userRequest.getState() != null) {
                existingUser.setState(userRequest.getState());
            }

            if (userRequest.getZipcode() != null) {
                existingUser.setZipcode(userRequest.getZipcode());
            }

            if (userRequest.getCountry() != null) {
                existingUser.setCountry(userRequest.getCountry());
            }


            if (image != null && !image.isEmpty()) {
                existingUser.setImage(image.getBytes());
            } else if (userRequest.getImage() != null) {

                existingUser.setImage(userRequest.getImage());
            }


            UserEntity updatedUser = userRepository.save(existingUser);


            Map<String, Object> claims = new HashMap<>();
            claims.put("email", updatedUser.getEmail());
            claims.put("contactNo", updatedUser.getContactNo());
            String token = jwtUtil.generateToken(claims, updatedUser.getEmail());

            return new AuthResponse(token, true, "User updated successfully", updatedUser);

        } catch (Exception e) {
            e.printStackTrace();
            return new AuthResponse(null, false, "Error updating user: " + e.getMessage(), null);
        }
    }

    public boolean deleteItem(Long id) {

        try {
            Optional<UserEntity> userOptional = userRepository.findById(id);

            if (userOptional.isPresent()) {
                userRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete item", e);
        }
    }

    /**
     * Gets or creates a default guest user for anonymous orders
     * @return A UserEntity that can be used for guest checkouts
     */
    public UserEntity getOrCreateGuestUser() {
        Optional<UserEntity> guestUserOptional = userRepository.findByEmail(GUEST_USER_EMAIL);
        
        if (guestUserOptional.isPresent()) {
            return guestUserOptional.get();
        } else {
            // Create a guest user
            UserEntity guestUser = new UserEntity();
            guestUser.setEmail(GUEST_USER_EMAIL);
            guestUser.setPassword(passwordEncoder.encode("guestUserPassword"));
            guestUser.setContactNo(0L); // Default contact number
            guestUser.setFirstName("Guest");
            guestUser.setLastName("User");
            
            return userRepository.save(guestUser);
        }
    }
}