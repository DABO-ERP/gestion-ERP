package com.daboerp.gestion.domain.valueobject;

/**
 * Reservation booking sources enumeration.
 */
public enum Source {
    DIRECT("Direct Booking"),
    BOOKING("Booking.com"),
    HOSTELWORLD("Hostelworld"),
    AIRBNB("Airbnb"),
    EXPEDIA("Expedia"),
    PHONE("Phone"),
    EMAIL("Email"),
    WALK_IN("Walk-in");
    
    private final String displayName;
    
    Source(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
