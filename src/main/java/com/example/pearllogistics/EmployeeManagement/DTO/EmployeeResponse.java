package com.example.pearllogistics.EmployeeManagement.DTO;
import com.example.pearllogistics.EmployeeManagement.Entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeResponse {
    private boolean success;
    private String message;
    private Employee employee;
}
