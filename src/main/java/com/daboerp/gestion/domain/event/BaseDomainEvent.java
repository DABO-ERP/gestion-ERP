package com.daboerp.gestion.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base class for domain events with common functionality.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseDomainEvent implements DomainEvent {
    
    @EqualsAndHashCode.Include
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    
    protected BaseDomainEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
    }
    
    protected BaseDomainEvent(UUID eventId, LocalDateTime occurredOn) {
        this.eventId = Objects.requireNonNull(eventId, "Event ID cannot be null");
        this.occurredOn = Objects.requireNonNull(occurredOn, "Occurred on cannot be null");
    }
}