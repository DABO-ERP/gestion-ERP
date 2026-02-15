package com.daboerp.gestion.application.decorator;

import com.daboerp.gestion.application.command.Command;
import com.daboerp.gestion.application.command.CommandHandler;

/**
 * Base decorator for command handlers.
 * Allows adding cross-cutting concerns without modifying core business logic.
 */
public abstract class CommandHandlerDecorator<C extends Command, R> implements CommandHandler<C, R> {
    
    protected final CommandHandler<C, R> decoratedHandler;
    
    protected CommandHandlerDecorator(CommandHandler<C, R> decoratedHandler) {
        this.decoratedHandler = decoratedHandler;
    }
    
    @Override
    public R handle(C command) {
        return decoratedHandler.handle(command);
    }
    
    @Override
    public String getSupportedCommandType() {
        return decoratedHandler.getSupportedCommandType();
    }
}