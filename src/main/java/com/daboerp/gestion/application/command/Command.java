package com.daboerp.gestion.application.command;

import java.util.UUID;

/**
 * Base interface for all application commands.
 * Commands represent operations that can change system state.
 */
public interface Command {
    
    /**
     * Unique identifier for command tracking and idempotency.
     */
    default UUID getCommandId() {
        return UUID.randomUUID();
    }
    
    /**
     * Command type for routing and validation.
     */
    String getCommandType();
}