package com.reliaquest.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    private WebClient webClient;

    private EmployeeService employeeService;

    public static MockWebServer mockServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockServer.getPort());
        webClient = WebClient.builder().baseUrl(baseUrl).build();
        employeeService = new EmployeeService(webClient);
    }

    @Test
    public void testGetAllEmployees() throws JsonProcessingException {

        Employee mockEmployee = Employee.builder().employeeName("Jimmy John").build();
        EmployeeResponseMultiple employeeResponse = new EmployeeResponseMultiple(List.of(mockEmployee), "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        List<Employee> response = employeeService.getAllEmployees();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Jimmy John", response.get(0).getEmployeeName());
    }

    @Test
    public void testGetEmployeesByNameSearch() throws JsonProcessingException {

        Employee mockEmployee1 = Employee.builder().employeeName("James Bond").build();
        Employee mockEmployee2 = Employee.builder().employeeName("Hetfield, James").build();

        EmployeeResponseMultiple employeeResponse = new EmployeeResponseMultiple(List.of(mockEmployee1, mockEmployee2), "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        List<Employee> response = employeeService.getEmployeesByNameSearch("James");

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    public void testGetEmployeesByNameSearch_notFound() throws JsonProcessingException {

        Employee mockEmployee = Employee.builder().employeeName("Albert Einstein").build();
        EmployeeResponseMultiple employeeResponse = new EmployeeResponseMultiple(List.of(mockEmployee), "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        List<Employee> response = employeeService.getEmployeesByNameSearch("Neutron");

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    public void testGetEmployeeById() throws JsonProcessingException {

        Employee mockEmployee = Employee.builder().employeeName("Albert Einstein").id("ff3eb839-d491-4632-bed9-d55ab70e2c0d").build();
        EmployeeResponseSingle employeeResponse = new EmployeeResponseSingle(mockEmployee, "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Employee response = employeeService.getEmployeeById("ff3eb839-d491-4632-bed9-d55ab70e2c0d");

        assertNotNull(response);
        assertEquals("Albert Einstein", response.getEmployeeName());

    }

    @Test
    public void testGetEmployeeById_notFound() throws JsonProcessingException {

        EmployeeResponseSingle employeeResponse = new EmployeeResponseSingle(null, "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Employee response = employeeService.getEmployeeById("Bohr");

        assertNull(response);
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws JsonProcessingException {

        List<Employee> mockEmployees = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            mockEmployees.add(Employee.builder().employeeSalary(i * 150).build());
        }
        mockEmployees.add(Employee.builder().employeeSalary(95000).build());

        EmployeeResponseMultiple employeeResponse = new EmployeeResponseMultiple(mockEmployees, "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Integer response = employeeService.getHighestSalaryOfEmployees();
        assertEquals(95000, response);
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws JsonProcessingException {

        List<Employee> mockEmployees = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            mockEmployees.add(Employee.builder().employeeSalary(i * 10000).employeeName("High Income Joe").build());
            mockEmployees.add(Employee.builder().employeeSalary(i).employeeName("Low Income Joe").build());
        }

        EmployeeResponseMultiple employeeResponse = new EmployeeResponseMultiple(mockEmployees, "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        List<String> response = employeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(10, response.size());
        assertEquals("High Income Joe", response.get(0));
        assertEquals("High Income Joe", response.get(response.size() - 1));
    }

    @Test
    public void testCreateEmployee() throws JsonProcessingException {

        CreateEmployeeInput employeeInput = new CreateEmployeeInput("Frank Grimes", 175000, 26, "Nuclear Safety Inspector");

        Employee mockEmployee = Employee.builder().employeeName("Frank Grimes").employeeTitle("Nuclear Safety Inspector").build();
        EmployeeResponseSingle employeeResponse = new EmployeeResponseSingle(mockEmployee, "success");

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Employee response = employeeService.createEmployee(employeeInput);

        assertNotNull(response);
        assertEquals("Frank Grimes", response.getEmployeeName());
        assertEquals("Nuclear Safety Inspector", response.getEmployeeTitle());

    }

    @Test
    public void testDeleteEmployeeById() throws JsonProcessingException {

        Employee employee = Employee.builder().employeeName("Alan Grant").build();
        EmployeeResponseSingle employeeResponseSingle = new EmployeeResponseSingle(employee, "success");

        EmployeeResponseBoolean employeeBooleanResponse = new EmployeeResponseBoolean();
        employeeBooleanResponse.setData(true);

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponseSingle))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeBooleanResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        String response = employeeService.deleteEmployeeById("d2e9f570-477a-4312-aa10-3c841b95f306");

        assertEquals("true", response);
    }

}
