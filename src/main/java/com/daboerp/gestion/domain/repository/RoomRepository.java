package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.RoomStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Room repository interface - domain layer contract.
 */
public interface RoomRepository {
    
    /**
     * Save a new room or update an existing one.
     */
    Room save(Room room);
    
    /**
     * Find a room by its unique identifier.
     */
    Optional<Room> findById(RoomId id);
    
    /**
     * Find a room by room number.
     */
    Optional<Room> findByRoomNumber(Integer roomNumber);
    
    /**
     * Find all rooms.
     */
    List<Room> findAll();
    
    /**
     * Find all rooms by status.
     */
    List<Room> findByStatus(RoomStatus status);
    
    /**
     * Find available rooms for a date range.
     */
    List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut);
    
    /**
     * Find available rooms by capacity.
     */
    List<Room> findAvailableByCapacity(int minCapacity, LocalDate checkIn, LocalDate checkOut);
    
    /**
     * Delete a room.
     */
    void delete(RoomId id);
    
    /**
     * Check if a room exists by room number.
     */
    boolean existsByRoomNumber(Integer roomNumber);
}
