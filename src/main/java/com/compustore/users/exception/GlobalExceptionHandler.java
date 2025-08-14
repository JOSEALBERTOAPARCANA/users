package com.compustore.users.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex){
    Map<String,String> errors = new HashMap<>();
    for (FieldError fe: ex.getBindingResult().getFieldErrors()) errors.put(fe.getField(), fe.getDefaultMessage());
    return ResponseEntity.badRequest().body(Map.of("status",400,"errors",errors));
  }
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String,Object>> handleDup(DataIntegrityViolationException ex){
    String detail = ex.getMostSpecificCause()!=null? ex.getMostSpecificCause().getMessage(): ex.getMessage();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status",409,"error","constraint violation","details",detail));
  }
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> handleIAE(IllegalArgumentException ex){
    return ResponseEntity.badRequest().body(Map.of("status",400,"error",ex.getMessage()));
  }
}
