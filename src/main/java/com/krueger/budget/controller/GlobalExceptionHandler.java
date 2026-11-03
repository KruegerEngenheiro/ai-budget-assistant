package com.krueger.budget.controller;

import com.krueger.budget.validation.InvalidCommitmentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Turns a broken business rule into a clean HTTP 400 with a readable message,
 * instead of a 500 stack trace.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCommitmentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidCommitment(InvalidCommitmentException exception) {
        return Map.of("error", exception.getMessage());
    }
}
