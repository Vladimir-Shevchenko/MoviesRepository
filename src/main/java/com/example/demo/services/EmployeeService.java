package com.example.demo.services;

import com.example.demo.models.Employee;
import com.example.demo.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public List<Employee> findEmployeesByRole(String role) {
        return employeeRepository.findByRole(role);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
