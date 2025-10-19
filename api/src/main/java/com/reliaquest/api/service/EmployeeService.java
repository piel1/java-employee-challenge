package com.reliaquest.api.service;

import com.reliaquest.api.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final WebClient webClient;

    public List<Employee> getAllEmployees() {

        EmployeeResponseList employeeList = webClient
                .get()
                .retrieve()
                .bodyToMono(EmployeeResponseList.class)
                .block();
        
        return employeeList != null ? employeeList.getData() : null;
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {

        List<Employee> employeeList = getAllEmployees();

        return employeeList
                .stream()
                .filter(e -> e.getEmployeeName().contains(searchString))
                .toList();
    }

    public Employee getEmployeeById(String id) {
        EmployeeResponseSingle employeeResponse = webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(EmployeeResponseSingle.class)
                .block();

        return employeeResponse != null ? employeeResponse.getData() : null;
    }

    public Integer getHighestSalaryOfEmployees() {

        List<Employee> employeeList = getAllEmployees();

        return employeeList
                .stream()
                .mapToInt(Employee::getEmployeeSalary)
                .max()
                .orElse(0);
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {

        List<Employee> employeeList = getAllEmployees();

        return employeeList
                .stream()
                .sorted(Comparator.comparing(Employee::getEmployeeSalary).reversed())
                .limit(10)
                .map(Employee::getEmployeeName)
                .toList();

    }

    public Employee createEmployee(CreateEmployeeInput employeeInput) {

        EmployeeResponseSingle createdEmployee = webClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeInput)
                .retrieve()
                .bodyToMono(EmployeeResponseSingle.class)
                .block();

        return createdEmployee != null ? createdEmployee.getData() : null;
    }

    public String deleteEmployeeById(String id) {

        Employee employee = getEmployeeById(id);

        DeleteEmployeeInput deleteEmployeeInput = new DeleteEmployeeInput(employee.getEmployeeName());

        EmployeeResponseBoolean response = webClient
                .method(HttpMethod.DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deleteEmployeeInput)
                .retrieve()
                .bodyToMono(EmployeeResponseBoolean.class)
                .block();

        return response != null ? response.getData().toString() : null;
    }
}
