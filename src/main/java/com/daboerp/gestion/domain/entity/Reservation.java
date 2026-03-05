package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.ReservationId;
import com.daboerp.gestion.domain.valueobject.Source;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Reservation aggregate root.
 * Represents a booking made by a guest.
 * No framework dependencies - pure domain model.
 */
public class Reservation {
    
    private final ReservationId id;
    private final String reservationCode;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;
    private BigDecimal quotedAmount;
    private Source source;
    private final LocalDate createdAt;
    private Guest guestPrincipal;
    private final List<Guest> guests;
    private Room room;
    private Stay stay;
    
    private Reservation(ReservationId id, String reservationCode, LocalDate checkIn, 
                       LocalDate checkOut, BigDecimal quotedAmount, Source source,
                       Guest guestPrincipal, Room room, LocalDate createdAt) {
        this.id = Objects.requireNonNull(id, "Reservation ID cannot be null");
        this.reservationCode = Objects.requireNonNull(reservationCode, "Reservation code cannot be null");
        this.checkIn = Objects.requireNonNull(checkIn, "Check-in date cannot be null");
        this.checkOut = Objects.requireNonNull(checkOut, "Check-out date cannot be null");
        this.quotedAmount = Objects.requireNonNull(quotedAmount, "Quoted amount cannot be null");
        this.source = Objects.requireNonNull(source, "Source cannot be null");
        this.guestPrincipal = Objects.requireNonNull(guestPrincipal, "Principal guest cannot be null");
        this.room = Objects.requireNonNull(room, "Room cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.guests = new ArrayList<>();
        this.guests.add(guestPrincipal);
        this.status = ReservationStatus.create(StatusType.CONFIRMED, "Reservation created");
        this.stay = null;
        
        validateDates(checkIn, checkOut);
        validateAmount(quotedAmount);
    }
    
    public static Reservation create(LocalDate checkIn, LocalDate checkOut, BigDecimal quotedAmount,
                                    Source source, Guest guestPrincipal, Room room) {
        ReservationId id = ReservationId.generate();
        String reservationCode = generateReservationCode();
        return new Reservation(id, reservationCode, checkIn, checkOut, quotedAmount,
                             source, guestPrincipal, room, LocalDate.now());
    }
    
    public static Reservation reconstitute(ReservationId id, String reservationCode, 
                                          LocalDate checkIn, LocalDate checkOut,
                                          ReservationStatus status, BigDecimal quotedAmount,
                                          Source source, Guest guestPrincipal, List<Guest> guests,
                                          Room room, Stay stay, LocalDate createdAt) {
        Reservation reservation = new Reservation(id, reservationCode, checkIn, checkOut,
                                                 quotedAmount, source, guestPrincipal, room, createdAt);
        reservation.status = status;
        reservation.stay = stay;
        if (guests != null) {
            reservation.guests.clear();
            reservation.guests.addAll(guests);
        }
        return reservation;
    }
    
    // Business methods
    public void addGuest(Guest guest) {
        Objects.requireNonNull(guest, "Guest cannot be null");
        if (!guests.contains(guest)) {
            guests.add(guest);
        }
    }
    
    public void removeGuest(Guest guest) {
        if (guest.equals(guestPrincipal)) {
            throw new IllegalArgumentException("Cannot remove principal guest");
        }
        guests.remove(guest);
    }
    
    public void confirm(String note) {
        this.status = ReservationStatus.create(StatusType.CONFIRMED, note);
    }
    
    public void cancel(String reason) {
        if (this.status.getStatusType() == StatusType.CHECKED_OUT) {
            throw new IllegalStateException("Cannot cancel a checked-out reservation");
        }
        this.status = ReservationStatus.create(StatusType.CANCELLED, reason);
        if (room != null) {
            room.markAsAvailable();
        }
    }
    
    public void checkIn(LocalDate actualCheckInDate) {
        if (this.status.getStatusType() != StatusType.CONFIRMED) {
            throw new IllegalStateException("Can only check-in confirmed reservations");
        }
        this.status = ReservationStatus.create(StatusType.CHECKED_IN, "Guest checked in");
        this.stay = Stay.create(actualCheckInDate, null);
        if (room != null) {
            room.markAsOccupied();
        }
    }
    
    public void checkOut(LocalDate actualCheckOutDate) {
        if (this.status.getStatusType() != StatusType.CHECKED_IN) {
            throw new IllegalStateException("Can only check-out checked-in reservations");
        }
        this.status = ReservationStatus.create(StatusType.CHECKED_OUT, "Guest checked out");
        if (this.stay != null) {
            this.stay = Stay.reconstitute(stay.getId(), stay.getCheckIn(), actualCheckOutDate);
        }
        if (room != null) {
            room.markAsAvailable();
        }
    }
    
    public void markAsNoShow(String reason) {
        if (this.status.getStatusType() == StatusType.CHECKED_IN || 
            this.status.getStatusType() == StatusType.CHECKED_OUT) {
            throw new IllegalStateException("Cannot mark as no-show after check-in");
        }
        this.status = ReservationStatus.create(StatusType.NO_SHOW, reason);
        if (room != null) {
            room.markAsAvailable();
        }
    }
    
    public void updateDates(LocalDate newCheckIn, LocalDate newCheckOut) {
        validateDates(newCheckIn, newCheckOut);
        this.checkIn = newCheckIn;
        this.checkOut = newCheckOut;
    }
    
    public void updateRoom(Room newRoom) {
        Objects.requireNonNull(newRoom, "Room cannot be null");
        this.room = newRoom;
    }
    
    public void updateQuotedAmount(BigDecimal newAmount) {
        validateAmount(newAmount);
        this.quotedAmount = newAmount;
    }
    
    public long getNightCount() {
        return java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }
    
    public boolean isActive() {
        return status.getStatusType() == StatusType.CONFIRMED || 
               status.getStatusType() == StatusType.CHECKED_IN;
    }
    
    private static String generateReservationCode() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }
    
    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quoted amount cannot be negative");
        }
    }
    
    // Getters
    public ReservationId getId() {
        return id;
    }
    
    public String getReservationCode() {
        return reservationCode;
    }
    
    public LocalDate getCheckIn() {
        return checkIn;
    }
    
    public LocalDate getCheckOut() {
        return checkOut;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public BigDecimal getQuotedAmount() {
        return quotedAmount;
    }
    
    public Source getSource() {
        return source;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public Guest getGuestPrincipal() {
        return guestPrincipal;
    }
    
    public List<Guest> getGuests() {
        return Collections.unmodifiableList(guests);
    }
    
    public Room getRoom() {
        return room;
    }
    
    public Stay getStay() {
        return stay;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
