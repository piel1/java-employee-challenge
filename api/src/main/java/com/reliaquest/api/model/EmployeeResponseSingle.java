package com.reliaquest.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class EmployeeResponseSingle {

    private Employee data;
    private String status;

}
