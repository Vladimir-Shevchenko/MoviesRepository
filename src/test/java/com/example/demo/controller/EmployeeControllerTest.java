package com.example.demo.controller;

import com.example.demo.models.Employee;
import com.example.demo.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EmployeeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EmployeeService employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        employeeService = mock(EmployeeService.class); // Manually mocking the service
        EmployeeController employeeController = new EmployeeController(employeeService);

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();

        testEmployee = new Employee(1L, "John Doe", "john.doe@example.com", "Developer", 30,"");
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        when(employeeService.findAll()).thenReturn(Arrays.asList(testEmployee));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void testGetEmployeesByRole() throws Exception {
        when(employeeService.findEmployeesByRole("Developer")).thenReturn(Arrays.asList(testEmployee));

        mockMvc.perform(get("/api/employees/role")
                        .param("role", "Developer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].role").value("Developer"));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        when(employeeService.save(any(Employee.class))).thenReturn(testEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee(1L, "Jane Doe", "jane.doe@example.com", "Manager", 35,"");
        when(employeeService.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeService.save(any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    public void testUpdateEmployeeNotFound() throws Exception {
        Employee updatedEmployee = new Employee(999L, "Jane Doe", "jane.doe@example.com", "Manager", 35,"");
        when(employeeService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        when(employeeService.findById(1L)).thenReturn(Optional.of(testEmployee));
        doNothing().when(employeeService).deleteById(1L);

        mockMvc.perform(delete("/api/employees/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee with ID 1 deleted successfully."));
    }

    @Test
    public void testDeleteEmployeeNotFound() throws Exception {
        when(employeeService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/employees/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee with ID 999 not found."));
    }
}

