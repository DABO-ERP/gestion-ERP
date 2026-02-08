package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomRepository;

import java.util.List;
import java.util.Objects;

/**
 * Use case for listing all rooms.
 */
public class ListRoomsUseCase {
    
    private final RoomRepository roomRepository;
    
    public ListRoomsUseCase(RoomRepository roomRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }
    
    public List<Room> execute() {
        return roomRepository.findAll();
    }
}
