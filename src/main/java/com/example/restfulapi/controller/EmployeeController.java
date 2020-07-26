package com.example.restfulapi.controller;

import com.example.restfulapi.exception.ResourceNotFoundException;
import com.example.restfulapi.model.Employee;
import com.example.restfulapi.repository.EmployeeRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    // GET employees

    @ApiOperation(value = "Get all employees", notes = "Get a list of all employees", response = Employee.class)
    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }


    @ApiOperation(value = "Get employee by ID", notes = "Get a list of all employees", response = Employee.class)
    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) throws ResourceNotFoundException {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found by this id"));
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Validated @RequestBody Employee employee) {
        return repository.save(employee);
    }
}
