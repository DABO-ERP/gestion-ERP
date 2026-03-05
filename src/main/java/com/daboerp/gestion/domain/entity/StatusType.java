package com.daboerp.gestion.domain.entity;

/**
 * StatusType enumeration - defines available reservation status types.
 */
public enum StatusType {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CHECKED_IN("Checked In"),
    CHECKED_OUT("Checked Out"),
    CANCELLED("Cancelled"),
    NO_SHOW("No Show");
    
    private final String displayName;
    
    StatusType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
