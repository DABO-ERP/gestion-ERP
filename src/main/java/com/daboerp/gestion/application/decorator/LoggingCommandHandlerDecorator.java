package com.daboerp.gestion.application.decorator;

import com.daboerp.gestion.application.command.Command;
import com.daboerp.gestion.application.command.CommandHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * Decorator that adds logging to command handlers.
 * Logs execution time, success, and failures without affecting business logic.
 */
public class LoggingCommandHandlerDecorator<C extends Command, R> extends CommandHandlerDecorator<C, R> {
    
    private final Logger logger;
    
    public LoggingCommandHandlerDecorator(CommandHandler<C, R> decoratedHandler) {
        super(decoratedHandler);
        this.logger = LoggerFactory.getLogger(decoratedHandler.getClass());
    }
    
    @Override
    public R handle(C command) {
        Instant start = Instant.now();
        String commandType = command.getCommandType();
        String commandId = command.getCommandId().toString();
        
        logger.info("Executing command: {} with ID: {}", commandType, commandId);
        
        try {
            R result = super.handle(command);
            
            Duration executionTime = Duration.between(start, Instant.now());
            logger.info("Command {} completed successfully in {}ms", 
                       commandType, executionTime.toMillis());
            
            return result;
            
        } catch (Exception e) {
            Duration executionTime = Duration.between(start, Instant.now());
            logger.error("Command {} failed after {}ms: {}", 
                        commandType, executionTime.toMillis(), e.getMessage(), e);
            throw e;
        }
    }
}