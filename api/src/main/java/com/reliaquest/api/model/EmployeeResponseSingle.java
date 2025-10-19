package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponseSingle {

    private Employee data;
    private String status;

}
