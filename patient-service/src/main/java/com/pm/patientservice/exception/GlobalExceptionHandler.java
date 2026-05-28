package com.pm.patientservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    // common helper to send custom error messages
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Map<String, String>> handleCommonException(CommonException ex){
    
        Map<String, String> errors = new HashMap<>();

        errors.put("Error", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);

    }

    // handle DTO validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
            error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);

    }
    
    // handle db email field error
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(DuplicateEmailException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("Error", "Email already registered");

        return ResponseEntity.badRequest().body(errors);

    }
    
}
