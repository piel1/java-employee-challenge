package com.reliaquest.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.EmployeeResponseMultiple;
import com.reliaquest.api.model.Employee;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        Employee mockEmployee = new Employee();
        mockEmployee.setEmployeeName("Pier-Jean Lizotte");
        EmployeeResponseMultiple employeeResponse = new EmployeeResponseMultiple(List.of(mockEmployee), "success");
        mockServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(employeeResponse))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        List<Employee> response = employeeService.getAllEmployees();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Pier-Jean Lizotte", response.get(0).getEmployeeName());

    }

}
