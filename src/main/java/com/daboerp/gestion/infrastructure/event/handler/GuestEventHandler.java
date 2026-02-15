package com.daboerp.gestion.infrastructure.event.handler;

import com.daboerp.gestion.domain.event.guest.GuestCreatedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for guest-related domain events.
 * Handles side effects and integrations when guest events occur.
 */
@Component
public class GuestEventHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GuestEventHandler.class);
    
    @EventListener
    public void handleGuestCreated(GuestCreatedEvent event) {
        logger.info("Guest created: {} {} with email: {}", 
                   event.getFirstName(), event.getLastName(), event.getEmail());
        
        // Here you could add additional processing:
        // - Send welcome email
        // - Create loyalty profile
        // - Notify external systems
        // - Update analytics
        
        processWelcomeWorkflow(event);
        updateCustomerAnalytics(event);
    }
    
    private void processWelcomeWorkflow(GuestCreatedEvent event) {
        // Simulate welcome email processing
        logger.debug("Processing welcome workflow for guest: {}", event.getGuestId());
        
        // In a real implementation, this would trigger:
        // - Email service to send welcome message
        // - CRM system updates
        // - Marketing automation
    }
    
    private void updateCustomerAnalytics(GuestCreatedEvent event) {
        // Simulate analytics update
        logger.debug("Updating customer analytics for new guest: {}", event.getGuestId());
        
        // In a real implementation, this would:
        // - Update business intelligence systems
        // - Increment customer counters
        // - Track acquisition sources
    }
}