package com.daboerp.gestion.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RoomBlock {
    private final String id;
    private final String roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private final LocalDateTime createdAt;

    public RoomBlock(String id, String roomId, LocalDate startDate, LocalDate endDate, String reason, LocalDateTime createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.createdAt = createdAt;
        validate();
    }

    private void validate() {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Block id is required");
        if (roomId == null || roomId.isBlank()) throw new IllegalArgumentException("Room id is required");
        if (startDate == null) throw new IllegalArgumentException("Start date is required");
        if (endDate == null) throw new IllegalArgumentException("End date is required");
        if (!endDate.isAfter(startDate)) throw new IllegalArgumentException("End date must be after start date");
    }

    public String getId() { return id; }
    public String getRoomId() { return roomId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public boolean coversDate(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) && date.isBefore(endDate);
    }

    public boolean overlaps(LocalDate start, LocalDate end) {
        return startDate.isBefore(end) && endDate.isAfter(start);
    }
}
