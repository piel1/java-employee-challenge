package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("GET /employee endpoint called");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        log.info("GET /employee/search/{} endpoint called", searchString);
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        log.info("GET /employee/{} endpoint called", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("GET /employee/highestSalary endpoint called");
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("GET /employee/topTenHighestEarningEmployeeNames endpoint called");
        return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
    }

    @PostMapping()
    ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeInput employeeInput) {
        log.info("POST /employee endpoint called");
        return ResponseEntity.ok(employeeService.createEmployee(employeeInput));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        log.info("DELETE /employee/{} endpoint called", id);
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }

}
