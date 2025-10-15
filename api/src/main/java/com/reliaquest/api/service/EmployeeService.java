package com.reliaquest.api.service;

import com.reliaquest.api.EmployeeResponse;
import com.reliaquest.api.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final WebClient webClient;

    public List<Employee> getAllEmployees() {

        EmployeeResponse employeeResponse = webClient
                .get()
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
                .block();

        return employeeResponse.getData();
    }
}
