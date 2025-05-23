package com.example.pearllogistics.EmployeeManagement.Controller;
import com.example.pearllogistics.EmployeeManagement.DTO.*;
import com.example.pearllogistics.EmployeeManagement.Entity.Employee;
import com.example.pearllogistics.EmployeeManagement.Service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true",
        allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<EmployeeResponse> registerEmployee(
            @RequestPart("employee") String employeeJson,
            @RequestPart("image") MultipartFile image) {

        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeRegisterDTO registerDTO;

        try {
            registerDTO = objectMapper.readValue(employeeJson, EmployeeRegisterDTO.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new EmployeeResponse( false, "Invalid employee JSON format", null));
        }

        try {
            EmployeeResponse response = employeeService.registerEmployee(registerDTO, image);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new EmployeeResponse( false, "Error processing image file", null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateEmployeeProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest request) {

        Employee updatedEmployee = employeeService.updateEmployee(id, request);

        if (updatedEmployee == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Employee not found with id: " + id, null));
        }

        // Convert to response format with limited fields
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", updatedEmployee.getId());
        responseData.put("name", updatedEmployee.getName());
        responseData.put("email", updatedEmployee.getEmail());
        responseData.put("role", updatedEmployee.getRole());

        // Convert byte[] to Base64 for frontend
        if (updatedEmployee.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(updatedEmployee.getImage());
            responseData.put("image", base64Image);
        }

        return ResponseEntity.ok(new ApiResponse(true, "Profile updated successfully", responseData));
    }


    @PostMapping("/login")
    public ResponseEntity<EmployeeResponse> loginEmployee(@RequestBody EmployeeLoginDTO loginDTO) {
        EmployeeResponse response = employeeService.loginEmployee(loginDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "get-employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteEmployee(@PathVariable Long id) {
        try {
            boolean deleted = employeeService.deleteEmployee(id);
            if (deleted) {
                return ResponseEntity.ok(
                        ResponseDTO.builder()
                                .status("success")
                                .message("Employee deleted successfully")
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDTO.builder()
                                .status("error")
                                .message("Employee not found")
                                .build()
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDTO.builder()
                            .status("error")
                            .message("Error deleting employee: " + e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/approve")
    public ResponseEntity<ResponseDTO> approveEmployee(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) int salary,
            @RequestParam(required = false) String role) {
        return new ResponseEntity<>(employeeService.approveEmployee(id, salary, role), HttpStatus.OK);
    }

    @GetMapping("/get-employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}