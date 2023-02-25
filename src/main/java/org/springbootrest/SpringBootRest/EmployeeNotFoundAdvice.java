package org.springbootrest.SpringBootRest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeNotFoundAdvice {
  @ResponseBody
  @ExceptionHandler
  @ResponseStatus
  String employeeNotFoundHandler(EmployeeNotFoundException ex){
    return ex.getMessage();
  }

}
