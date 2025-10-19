package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateEmployeeInput {

    private String name;

    private Integer salary;

    private Integer age;

    private String title;
}
