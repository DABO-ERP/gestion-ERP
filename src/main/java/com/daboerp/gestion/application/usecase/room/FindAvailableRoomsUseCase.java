package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Use case for finding available rooms.
 */
public class FindAvailableRoomsUseCase {
    
    private final RoomRepository roomRepository;
    
    public FindAvailableRoomsUseCase(RoomRepository roomRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }
    
    public List<Room> execute(FindAvailableRoomsQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        
        if (query.checkIn() != null && query.checkOut() != null) {
            if (query.minCapacity() != null && query.minCapacity() > 0) {
                return roomRepository.findAvailableByCapacity(
                    query.minCapacity(),
                    query.checkIn(),
                    query.checkOut()
                );
            }
            return roomRepository.findAvailableRooms(query.checkIn(), query.checkOut());
        }
        
        // Return currently available rooms
        return roomRepository.findByStatus(RoomStatus.AVAILABLE);
    }
    
    public record FindAvailableRoomsQuery(
        LocalDate checkIn,
        LocalDate checkOut,
        Integer minCapacity
    ) {}
}
