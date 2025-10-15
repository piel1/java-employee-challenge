package com.reliaquest.api.service;

import com.reliaquest.api.model.EmployeeResponseMultiple;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeResponseSingle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final WebClient webClient;

    public List<Employee> getAllEmployees() {

        EmployeeResponseMultiple employeeResponse = webClient
                .get()
                .retrieve()
                .bodyToMono(EmployeeResponseMultiple.class)
                .block();

        return employeeResponse.getData();
    }

    public Employee getEmployeeById(String id) {
        EmployeeResponseSingle employeeResponse = webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeResponseSingle.class)
                .block();

        return employeeResponse.getData();
    }
}
