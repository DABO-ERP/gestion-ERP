package com.daboerp.gestion.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base interface for all domain events.
 * Domain events represent something that happened in the domain 
 * that other parts of the same domain (in-process) are interested in.
 */
public interface DomainEvent {
    
    /**
     * Unique identifier for this event instance.
     */
    UUID getEventId();
    
    /**
     * When this event occurred.
     */
    LocalDateTime getOccurredOn();
    
    /**
     * Version of the event for future compatibility.
     */
    default int getVersion() {
        return 1;
    }
    
    /**
     * Type of the event for routing and handling.
     */
    String getEventType();
}