package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.entity.StatusType;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.ReservationId;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Reservation repository interface - domain layer contract.
 */
public interface ReservationRepository {
    
    /**
     * Save a new reservation or update an existing one.
     */
    Reservation save(Reservation reservation);
    
    /**
     * Find a reservation by its unique identifier.
     */
    Optional<Reservation> findById(ReservationId id);
    
    /**
     * Find a reservation by reservation code.
     */
    Optional<Reservation> findByReservationCode(String reservationCode);
    
    /**
     * Find all reservations.
     */
    List<Reservation> findAll();
    
    /**
     * Find reservations by guest.
     */
    List<Reservation> findByGuest(GuestId guestId);
    
    /**
     * Find reservations by room.
     */
    List<Reservation> findByRoom(RoomId roomId);
    
    /**
     * Find reservations by status.
     */
    List<Reservation> findByStatus(StatusType statusType);
    
    /**
     * Find reservations for a specific date range.
     */
    List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find active reservations (confirmed or checked-in).
     */
    List<Reservation> findActiveReservations();
    
    /**
     * Find reservations checking in on a specific date.
     */
    List<Reservation> findCheckInsForDate(LocalDate date);
    
    /**
     * Find reservations checking out on a specific date.
     */
    List<Reservation> findCheckOutsForDate(LocalDate date);
    
    /**
     * Find overlapping reservations for a room and date range.
     */
    List<Reservation> findOverlappingReservations(RoomId roomId, LocalDate checkIn, LocalDate checkOut);
    
    /**
     * Delete a reservation.
     */
    void delete(ReservationId id);
}
