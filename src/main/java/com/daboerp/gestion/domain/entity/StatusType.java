package com.daboerp.gestion.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * StatusType enumeration - defines available reservation status types.
 */
@Getter
@RequiredArgsConstructor
public enum StatusType {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    CHECKED_IN("Checked In"),
    CHECKED_OUT("Checked Out"),
    CANCELLED("Cancelled"),
    NO_SHOW("No Show");
    
    private final String displayName;
}
