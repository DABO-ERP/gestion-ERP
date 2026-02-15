package com.daboerp.gestion.domain.event;

/**
 * Domain Event Publisher interface.
 * Allows domain entities to publish events without depending on infrastructure.
 */
public interface DomainEventPublisher {
    
    /**
     * Publish a domain event.
     */
    void publish(DomainEvent event);
    
    /**
     * Publish multiple domain events.
     */
    default void publishAll(Iterable<DomainEvent> events) {
        events.forEach(this::publish);
    }
}