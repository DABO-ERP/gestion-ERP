package com.daboerp.gestion.domain.event.guest;

import com.daboerp.gestion.domain.event.BaseDomainEvent;
import com.daboerp.gestion.domain.valueobject.GuestId;
import lombok.Getter;

/**
 * Event published when a new guest is created.
 */
@Getter
public class GuestCreatedEvent extends BaseDomainEvent {
    
    private final GuestId guestId;
    private final String email;
    private final String firstName;
    private final String lastName;
    
    public GuestCreatedEvent(GuestId guestId, String email, String firstName, String lastName) {
        super();
        this.guestId = guestId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    @Override
    public String getEventType() {
        return "guest.created";
    }
}