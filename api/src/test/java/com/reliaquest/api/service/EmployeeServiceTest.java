package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testGetAllEmployees() {

        List<Employee> response = employeeService.getAllEmployees();

        assertNull(response);

    }

}
