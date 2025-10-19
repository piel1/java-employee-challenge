package com.reliaquest.api.service;

import com.reliaquest.api.exception.EmployeeServerException;
import com.reliaquest.api.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final WebClient webClient;

    public List<Employee> getAllEmployees() {

        log.info("Getting all employees from employee server");

        EmployeeResponseList employeeResponse = webClient
                .get()
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> {
                            log.error("There was an error with the employee server: {}", response.statusCode());
                            return Mono.error(new EmployeeServerException(String.format("There was an error with the employee server: %s", response.statusCode())));
                        }
                )
                .bodyToMono(EmployeeResponseList.class)
                .block();

        assert employeeResponse != null;

        log.info("Fetched {} employees", employeeResponse.getData().size());
        return employeeResponse.getData();
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {

        log.info("Searching employees matching name {}", searchString);

        List<Employee> employeeList = getAllEmployees();

        List<Employee> response = employeeList
                .stream()
                .filter(e -> e.getEmployeeName().contains(searchString))
                .toList();

        log.info("Found {} matching employees", response.size());
        return response;
    }

    public Employee getEmployeeById(String id) {

        log.info("Getting employee for id {}", id);

        EmployeeResponseSingle employeeResponse = webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> {
                            log.error("There was an error with the employee server: {}", response.statusCode());
                            return Mono.error(new EmployeeServerException(String.format("There was an error with the employee server: %s", response.statusCode())));
                        }
                )
                .bodyToMono(EmployeeResponseSingle.class)
                .block();

        return employeeResponse != null ? employeeResponse.getData() : null;
    }

    public Integer getHighestSalaryOfEmployees() {

        log.info("Fetching highest salary of all employees");

        List<Employee> employeeList = getAllEmployees();

        return employeeList
                .stream()
                .mapToInt(Employee::getEmployeeSalary)
                .max()
                .orElse(0);
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {

        log.info("Fetching top 10 employee names with highest salary");

        List<Employee> employeeList = getAllEmployees();

        return employeeList
                .stream()
                .sorted(Comparator.comparing(Employee::getEmployeeSalary).reversed())
                .limit(10)
                .map(Employee::getEmployeeName)
                .toList();

    }

    public Employee createEmployee(CreateEmployeeInput employeeInput) {

        log.info("Creating employee {}", employeeInput);

        EmployeeResponseSingle createdEmployee = webClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeInput)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> {
                            log.error("There was an error with the employee server: {}", response.statusCode());
                            return Mono.error(new EmployeeServerException(String.format("There was an error with the employee server: %s", response.statusCode())));
                        }
                )
                .bodyToMono(EmployeeResponseSingle.class)
                .block();

        return createdEmployee != null ? createdEmployee.getData() : null;
    }

    public String deleteEmployeeById(String id) {

        log.info("Deleting employee with id {}", id);

        Employee employee = getEmployeeById(id);

        DeleteEmployeeInput deleteEmployeeInput = new DeleteEmployeeInput(employee.getEmployeeName());
        EmployeeResponseBoolean employeeResponse = webClient
                .method(HttpMethod.DELETE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deleteEmployeeInput)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            log.error("There was an error with the employee server: {}", clientResponse.statusCode());
                            return Mono.error(new EmployeeServerException(String.format("There was an error with the employee server: %s", clientResponse.statusCode())));
                        }
                )
                .bodyToMono(EmployeeResponseBoolean.class)
                .block();

        return employeeResponse != null ? employeeResponse.getData().toString() : null;
    }
}
