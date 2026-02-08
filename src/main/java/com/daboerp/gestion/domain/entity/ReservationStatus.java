package com.daboerp.gestion.domain.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * ReservationStatus entity - tracks the current state of a reservation.
 */
public class ReservationStatus {
    
    private final String id;
    private final StatusType statusType;
    private final String note;
    
    private ReservationStatus(String id, StatusType statusType, String note) {
        this.id = Objects.requireNonNull(id, "Status ID cannot be null");
        this.statusType = Objects.requireNonNull(statusType, "Status type cannot be null");
        this.note = note;
    }
    
    public static ReservationStatus create(StatusType statusType, String note) {
        return new ReservationStatus(UUID.randomUUID().toString(), statusType, note);
    }
    
    public static ReservationStatus reconstitute(String id, StatusType statusType, String note) {
        return new ReservationStatus(id, statusType, note);
    }
    
    public String getId() {
        return id;
    }
    
    public StatusType getStatusType() {
        return statusType;
    }
    
    public String getNote() {
        return note;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationStatus that = (ReservationStatus) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
