package com.reliaquest.api;

import com.reliaquest.api.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class EmployeeResponse {

    public List<Employee> data;

}
