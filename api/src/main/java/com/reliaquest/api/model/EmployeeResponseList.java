package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeResponseList {

    private List<Employee> data;
    private String status;

}
