package com.daboerp.gestion.application.command;

/**
 * Generic command handler interface.
 * Handles specific command types and produces results.
 */
public interface CommandHandler<C extends Command, R> {
    
    /**
     * Handle the command and return a result.
     */
    R handle(C command);
    
    /**
     * Get the command type this handler supports.
     */
    String getSupportedCommandType();
    
    /**
     * Check if this handler can handle the given command.
     */
    default boolean canHandle(Command command) {
        return getSupportedCommandType().equals(command.getCommandType());
    }
}