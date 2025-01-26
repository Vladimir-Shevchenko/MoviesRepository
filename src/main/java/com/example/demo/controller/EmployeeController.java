package com.example.demo.controller;

import com.example.demo.models.Employee;
import com.example.demo.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    @Operation(summary = "Get all employees", description = "Fetches all employees from the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of employees")
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @Operation(summary = "Get employees by role", description = "Fetches employees based on their role")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of employees by role")
    @GetMapping("/role")
    public List<Employee> getEmployeesByRole(@RequestParam String role) {
        return employeeService.findEmployeesByRole(role);
    }

    @Operation(summary = "Create a new employee", description = "Creates a new employee record in the database")
    @ApiResponse(responseCode = "201", description = "Successfully created a new employee")
    @ApiResponse(responseCode = "400", description = "Invalid employee details")
    @PostMapping
    public Employee createEmployee(@RequestBody @Parameter(description = "Employee details to be created") Employee employee) {
        return employeeService.save(employee);
    }

    @Operation(summary = "Update an existing employee", description = "Updates the details of an existing employee")
    @ApiResponse(responseCode = "200", description = "Successfully updated the employee")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        Optional<Employee> existingEmployee = employeeService.findById(id);

        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            // Update the fields
            employee.setName(updatedEmployee.getName());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setRole(updatedEmployee.getRole());

            // Save the updated employee back to the database
            Employee savedEmployee = employeeService.save(employee);
            return ResponseEntity.ok(savedEmployee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Return 404 if the employee doesn't exist
        }
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee from the database")
    @ApiResponse(responseCode = "200", description = "Successfully deleted the employee")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);

        if (employee.isPresent()) {
            employeeService.deleteById(id);
            return ResponseEntity.ok("Employee with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + id + " not found.");
        }
    }

}
