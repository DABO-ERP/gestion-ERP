package com.daboerp.gestion.application.decorator;

import com.daboerp.gestion.application.command.Command;
import com.daboerp.gestion.application.command.CommandHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Decorator that adds validation to command handlers.
 * Validates commands using Bean Validation before execution.
 */
public class ValidationCommandHandlerDecorator<C extends Command, R> extends CommandHandlerDecorator<C, R> {
    
    private final Validator validator;
    
    public ValidationCommandHandlerDecorator(CommandHandler<C, R> decoratedHandler, Validator validator) {
        super(decoratedHandler);
        this.validator = validator;
    }
    
    @Override
    public R handle(C command) {
        validateCommand(command);
        return super.handle(command);
    }
    
    private void validateCommand(C command) {
        Set<ConstraintViolation<C>> violations = validator.validate(command);
        
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
                
            throw new IllegalArgumentException("Command validation failed: " + errorMessage);
        }
    }
}