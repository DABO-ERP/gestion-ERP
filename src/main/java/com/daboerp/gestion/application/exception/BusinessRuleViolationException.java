package com.daboerp.gestion.application.exception;

/**
 * Exception thrown when business rules are violated.
 */
public class BusinessRuleViolationException extends ApplicationException {
    
    public BusinessRuleViolationException(String message) {
        super(message);
    }
    
    public BusinessRuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
