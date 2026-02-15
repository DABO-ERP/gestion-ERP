package com.daboerp.gestion.infrastructure.event;

import com.daboerp.gestion.domain.event.DomainEvent;
import com.daboerp.gestion.domain.event.DomainEventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Spring-based implementation of DomainEventPublisher.
 * Bridges domain events to Spring's application event mechanism.
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringDomainEventPublisher.class);
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher, 
            "Application event publisher cannot be null");
    }
    
    @Override
    public void publish(DomainEvent event) {
        Objects.requireNonNull(event, "Domain event cannot be null");
        
        logger.debug("Publishing domain event: {} with ID: {}", 
                    event.getEventType(), event.getEventId());
        
        try {
            applicationEventPublisher.publishEvent(event);
            logger.debug("Domain event published successfully: {}", event.getEventType());
        } catch (Exception e) {
            logger.error("Failed to publish domain event: {} - {}", event.getEventType(), e.getMessage(), e);
            throw new EventPublicationException("Failed to publish domain event", e);
        }
    }
    
    /**
     * Exception indicating domain event publication failure.
     */
    public static class EventPublicationException extends RuntimeException {
        public EventPublicationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}