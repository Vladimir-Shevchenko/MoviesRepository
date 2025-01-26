package com.example.demo.services;

import com.example.demo.models.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        // Create and save an Employee object
        employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setRole("Director");
        employee.setAge(30);

        employee = employeeRepository.save(employee);
    }

    @Test
    public void testSaveEmployee() {
        // Create a new employee object
        Employee newEmployee = new Employee();
        newEmployee.setName("Jane Smith");
        newEmployee.setEmail("jane.smith@example.com");
        newEmployee.setRole("Producer");
        newEmployee.setAge(28);

        // Save the new employee using the service
        Employee savedEmployee = employeeService.save(newEmployee);

        // Assertions
        assertNotNull(savedEmployee.getId());
        assertEquals(newEmployee.getName(), savedEmployee.getName());
        assertEquals(newEmployee.getEmail(), savedEmployee.getEmail());
    }

    @Test
    public void testFindAllEmployees() {
        // Call the findAll method from the service
        List<Employee> employees = employeeService.findAll();

        // Assertions
        assertTrue(employees.size() > 0);  // Ensure at least one employee is returned
        assertTrue(employees.contains(employee));  // Ensure the employee we created is in the list
    }

    @Test
    public void testFindEmployeesByRole() {
        // Call the findEmployeesByRole method from the service
        List<Employee> directors = employeeService.findEmployeesByRole("Director");

        // Assertions
        assertTrue(directors.size() > 0);  // Ensure the list is not empty
        assertTrue(directors.stream().anyMatch(e -> e.getRole().equals("Director")));  // Ensure at least one employee has the role "Director"
    }

    @Test
    public void testFindEmployeeById() {
        // Call the findById method from the service
        Optional<Employee> foundEmployee = employeeService.findById(employee.getId());

        // Assertions
        assertTrue(foundEmployee.isPresent());
        assertEquals(employee.getId(), foundEmployee.get().getId());
    }

    @Test
    public void testDeleteEmployeeById() {
        // Delete the employee
        employeeService.deleteById(employee.getId());

        // Verify the employee is deleted by trying to find it
        Optional<Employee> deletedEmployee = employeeService.findById(employee.getId());
        assertFalse(deletedEmployee.isPresent());
    }
}
