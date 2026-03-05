package com.daboerp.gestion.application.exception;

/**
 * Base exception for all application layer exceptions.
 */
public abstract class ApplicationException extends RuntimeException {
    
    protected ApplicationException(String message) {
        super(message);
    }
    
    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
