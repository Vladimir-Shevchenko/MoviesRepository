package com.example.demo.services;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.models.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Employee createValidEmployee() {
        return new Employee(
                1L,                    // id
                "John Doe",            // name
                "john.doe@example.com", // email
                "Developer",           // role
                30,                    // age
                "password123"          // password (null in most cases if not needed)
        );
    }

    @Test
    public void testFindAll() {
        // Given
        Employee employee1 = createValidEmployee();
        Employee employee2 = new Employee(2L, "Jane Doe", "jane.doe@example.com", "Manager", 35, "password123");
        List<Employee> employees = Arrays.asList(employee1, employee2);

        // When
        when(employeeRepository.findAll()).thenReturn(employees);

        // Then
        List<Employee> result = employeeService.findAll();
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testFindEmployeesByRole() {
        // Given
        Employee employee = createValidEmployee();
        List<Employee> employees = Arrays.asList(employee);

        // When
        when(employeeRepository.findByRole("Developer")).thenReturn(employees);

        // Then
        List<Employee> result = employeeService.findEmployeesByRole("Developer");
        assertEquals(1, result.size());
        assertEquals("Developer", result.get(0).getRole());
        verify(employeeRepository, times(1)).findByRole("Developer");
    }

    @Test
    public void testSaveEmployee() {
        // Given
        Employee employee = createValidEmployee();
        when(employeeRepository.save(employee)).thenReturn(employee);

        // When
        Employee result = employeeService.save(employee);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("Developer", result.getRole());
        assertEquals(30, result.getAge());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testFindById() {
        // Given
        Employee employee = createValidEmployee();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // When
        Optional<Employee> result = employeeService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        // Given
        doNothing().when(employeeRepository).deleteById(1L);

        // When
        employeeService.deleteById(1L);

        // Then
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
