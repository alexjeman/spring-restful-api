package com.example.restfulapi.controller;

import com.example.restfulapi.exception.ResourceNotFoundException;
import com.example.restfulapi.model.Employee;
import com.example.restfulapi.repository.EmployeeRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1")
class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }


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


    @ApiOperation(value = "Add employee to database", notes = "Create a employee entry", response = Employee.class)
    @PostMapping("/employees")
    Employee createEmployee(@Validated @RequestBody Employee employee) {
        return repository.save(employee);
    }


    @ApiOperation(value = "Update employee by ID", notes = "Update a employee entry in database", response = Employee.class)
    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id).map(employee -> {
            employee.setFirstname(newEmployee.getFirstname());
            employee.setLastname(newEmployee.getLastname());
            employee.setEmail(newEmployee.getEmail());
            return repository.save(employee);
        }).orElseGet(() -> {
            newEmployee.setId(id);
            return repository.save(newEmployee);
        });
    }


    @ApiOperation(value = "Delete employee by ID", notes = "Delete employee from database", response = Employee.class)
    @DeleteMapping("/employees/{id}")
    Map<String, Boolean> deleteEmployee(@PathVariable Long id) throws ResourceNotFoundException {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found by this id"));
        repository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
