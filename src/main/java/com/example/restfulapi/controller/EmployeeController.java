package com.example.restfulapi.controller;

import com.example.restfulapi.model.Employee;
import com.example.restfulapi.repository.EmployeeRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // GET employees

    @GetMapping(path = "/employees")
    @ApiOperation(value = "Get all employees", notes = "Get a list of all employees", response = Employee.class)
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

}
