package org.springbootrest.SpringBootRest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
  private final EmployeeRepository repository;

  EmployeeController(EmployeeRepository repository){
    this.repository = repository;
  }

  @GetMapping("/employees")
  CollectionModel<EntityModel<Employee>> all(){
    List<EntityModel<Employee>> employees = repository.findAll().stream().map(
      employee -> EntityModel.of(employee,
        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
        linkTo(methodOn(EmployeeController.class).all()).withRel("employees"))).toList();
    return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
  }

  @GetMapping("employees/{id}")
  EntityModel<Employee> one(@PathVariable Long id){
    Employee employee = repository.findById(id).orElseThrow(
        () -> new EmployeeNotFoundException(id)
    );

    return EntityModel.of(
        employee,
        linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
    );
  }

  @PostMapping("/employees")
  Employee newEmployee(@RequestBody Employee newEmployee){
    return this.repository.save(newEmployee);
  }

  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
    return repository.findById(id).map(employee -> {
      employee.setName(newEmployee.getName());
      employee.setRole(newEmployee.getRole());
      return repository.save(employee);
    }).orElseGet(() -> {
      newEmployee.setId(id);
      return repository.save(newEmployee);
    });
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id){
    this.repository.deleteById(id);
  }
}