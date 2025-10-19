package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testGetAllEmployees() throws Exception {

        Employee employee = Employee.builder().employeeName("John Connor").employeeAge(25).build();

        List<Employee> mockEmployees = List.of(employee);

        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name", is("John Connor")));

    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {

        Employee employee1 = Employee.builder().employeeName("Homer Simpson").build();
        Employee employee2 = Employee.builder().employeeName("Marge Simpson").build();

        List<Employee> mockEmployees = List.of(employee1, employee2);

        when(employeeService.getEmployeesByNameSearch("Simpson")).thenReturn(mockEmployees);

        mockMvc.perform(get("/employee/search/{0}", "Simpson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name", is("Homer Simpson")))
                .andExpect(jsonPath("$[1].employee_name", is("Marge Simpson")));

    }

    @Test
    public void testGetEmployeeById() throws Exception {

        Employee mockEmployee = Employee.builder().employeeName("Sarah Connor").id("abc-123").build();

        when(employeeService.getEmployeeById("abc-123")).thenReturn(mockEmployee);

        mockMvc.perform(get("/employee/{0}", "abc-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("abc-123")))
                .andExpect(jsonPath("$.employee_name", is("Sarah Connor")));
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(925000);

        mockMvc.perform(get("/employee/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(925000)));
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {

        List<String> employeeNames = Arrays.asList("John", "Paul", "Ringo", "George");

        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(employeeNames);

        mockMvc.perform(get("/employee/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(4)))
                .andExpect(jsonPath("$[0]", is("John")))
                .andExpect(jsonPath("$[1]", is("Paul")))
                .andExpect(jsonPath("$[2]", is("Ringo")))
                .andExpect(jsonPath("$[3]", is("George")));
    }

    @Test
    public void testCreateEmployee() throws Exception {

        CreateEmployeeInput employeeInput = new CreateEmployeeInput("John Smith", 150000, 59, "Accountant");

        Employee mockEmployee = Employee.builder().employeeName("John Smith").id("abc-123").employeeSalary(150000).build();

        when(employeeService.createEmployee(any())).thenReturn(mockEmployee);

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employeeInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("abc-123")))
                .andExpect(jsonPath("$.employee_name", is("John Smith")))
                .andExpect(jsonPath("$.employee_salary", is(150000)));
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {

        when(employeeService.deleteEmployeeById(any())).thenReturn("true");

        mockMvc.perform(delete("/employee/{0}", "abc-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }
}
