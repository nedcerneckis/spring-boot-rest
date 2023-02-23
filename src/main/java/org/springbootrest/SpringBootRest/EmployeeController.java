package org.springbootrest.SpringBootRest;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
  private final EmployeeRepository repository;

  EmployeeController(EmployeeRepository repository){
    this.repository = repository;
  }

  @GetMapping("/employees")
  List<Employee> all(){
    return this.repository.findAll();
  }

  @GetMapping("employees/{id}")
  Employee one(@PathVariable Long id){
    return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
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