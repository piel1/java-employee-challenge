package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Employee {

    private String id;
    private String employeeName;
    private int employeeSalary;
    private int employeeAge;
    private String employeeTitle;
    private String employeeEmail;
}
