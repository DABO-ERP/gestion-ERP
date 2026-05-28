package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.RoomBlockId;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.time.LocalDate;
import java.util.Objects;

public class RoomBlock {

    private final RoomBlockId id;
    private final RoomId roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private boolean active;
    private final LocalDate createdAt;

    private RoomBlock(RoomBlockId id, RoomId roomId, LocalDate startDate, LocalDate endDate, String reason, LocalDate createdAt) {
        this.id = Objects.requireNonNull(id, "RoomBlock ID cannot be null");
        this.roomId = Objects.requireNonNull(roomId, "Room ID cannot be null");
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
        this.endDate = Objects.requireNonNull(endDate, "End date cannot be null");
        this.reason = reason;
        this.active = true;
        this.createdAt = createdAt != null ? createdAt : LocalDate.now();
        validate();
    }

    public static RoomBlock create(RoomId roomId, LocalDate startDate, LocalDate endDate, String reason) {
        return new RoomBlock(RoomBlockId.generate(), roomId, startDate, endDate, reason, LocalDate.now());
    }

    public static RoomBlock reconstitute(RoomBlockId id, RoomId roomId, LocalDate startDate, LocalDate endDate,
                                          String reason, boolean active, LocalDate createdAt) {
        RoomBlock block = new RoomBlock(id, roomId, startDate, endDate, reason, createdAt);
        block.active = active;
        return block;
    }

    private void validate() {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }
    }

    public boolean overlaps(LocalDate checkIn, LocalDate checkOut) {
        return active && !(endDate.isBefore(checkIn) || startDate.isAfter(checkOut));
    }

    public void deactivate() {
        this.active = false;
    }

    public void updateDates(LocalDate startDate, LocalDate endDate) {
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
        this.endDate = Objects.requireNonNull(endDate, "End date cannot be null");
        validate();
    }

    public void updateReason(String reason) {
        this.reason = reason;
    }

    public RoomBlockId getId() { return id; }
    public RoomId getRoomId() { return roomId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getReason() { return reason; }
    public boolean isActive() { return active; }
    public LocalDate getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomBlock roomBlock = (RoomBlock) o;
        return Objects.equals(id, roomBlock.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
