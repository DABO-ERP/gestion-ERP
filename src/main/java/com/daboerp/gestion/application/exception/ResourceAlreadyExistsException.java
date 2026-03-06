package com.daboerp.gestion.application.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class ResourceAlreadyExistsException extends ApplicationException {
    
    public ResourceAlreadyExistsException(String resourceType, String identifier) {
        super(String.format("%s already exists with identifier: %s", resourceType, identifier));
    }
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
