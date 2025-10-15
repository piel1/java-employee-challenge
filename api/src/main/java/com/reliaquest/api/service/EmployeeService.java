package com.reliaquest.api.service;

import com.reliaquest.api.EmployeeResponse;
import com.reliaquest.api.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    public List<Employee> getAllEmployees() {

        EmployeeResponse employeeList = new EmployeeResponse();

        return employeeList.getData();
    }
}
