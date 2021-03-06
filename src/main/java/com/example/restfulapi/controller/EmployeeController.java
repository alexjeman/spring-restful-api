package com.example.restfulapi.controller;

import com.example.restfulapi.exception.ResourceNotFoundException;
import com.example.restfulapi.model.Employee;
import com.example.restfulapi.repository.EmployeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {

        this.repository = repository;
    }


    @Operation(summary = "Get all employees", description = "Get a list of all employees")
    @GetMapping("/employees")
    public CollectionModel<Employee> getAllEmployees() throws ResourceNotFoundException {

        List<Employee> employees = repository.findAll();

        for (Employee employee : employees) {
            Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel();
            employee.add(link);
        }
        Link linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getAllEmployees()).withSelfRel();

        return CollectionModel.of(employees, linkSelf);
    }


    @Operation(summary = "Get employee by ID", description = "Get a list of all employees")
    @GetMapping("/employees/{id}")
    public EntityModel<Employee> getEmployee(@PathVariable Long id) throws ResourceNotFoundException {

        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getEmployee(id)).withSelfRel();
        Link linkBack = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getAllEmployees()).withSelfRel();
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found by this id"));
        employee.add(link);
        employee.add(linkBack);
        return EntityModel.of(employee);
    }


    @Operation(summary = "Add employee to database", description = "Create a employee entry")
    @PostMapping("/employees")
    Employee createEmployee(@Validated @RequestBody Employee employee) {
        return repository.save(employee);
    }


    @Operation(summary = "Update employee by ID", description = "Update a employee entry in database")
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


    @Operation(summary = "Delete employee by ID", description = "Delete employee from database")
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
