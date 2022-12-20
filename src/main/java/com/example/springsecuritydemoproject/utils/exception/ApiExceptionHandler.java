package com.example.springsecuritydemoproject.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    /**
     * Spting validation exception handling
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleApiRequestValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

}
