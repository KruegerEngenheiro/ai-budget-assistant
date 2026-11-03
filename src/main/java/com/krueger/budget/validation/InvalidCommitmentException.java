package com.krueger.budget.validation;

/**
 * Thrown when a budget commitment breaks a business rule and must not be saved.
 */
public class InvalidCommitmentException extends RuntimeException {

    public InvalidCommitmentException(String message) {
        super(message);
    }
}
