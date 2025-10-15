package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void testGetAllEmployees() {

        when(employeeService.getAllEmployees()).thenReturn(null);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertNotNull(response);
    }

    @Test
    public void testGetEmployeeById() {
        ResponseEntity<Employee> response = employeeController.getEmployeeById("abc");

        assertNull(response);
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertNotNull(response);
        assertEquals(17, response.getBody());
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertNull(response);
    }

    @Test
    public void testCreateEmployee() {
        ResponseEntity<Employee> response = employeeController.createEmployee(new CreateEmployeeInput());

        assertNull(response);
    }

    @Test
    public void testDeleteEmployeeById() {
        ResponseEntity<String> response = employeeController.deleteEmployeeById("abc");

        assertNull(response);
    }
}
