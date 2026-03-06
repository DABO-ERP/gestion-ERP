package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.RoomTypeId;

import java.util.List;
import java.util.Objects;

/**
 * Use case for creating a new room.
 */
public class CreateRoomUseCase {
    
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    
    public CreateRoomUseCase(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
        this.roomTypeRepository = Objects.requireNonNull(roomTypeRepository, "Room type repository cannot be null");
    }
    
    public Room execute(CreateRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        
        // Validate room doesn't already exist
        if (roomRepository.existsByRoomNumber(command.roomNumber())) {
            throw new ResourceAlreadyExistsException("Room", command.roomNumber().toString());
        }
        
        // Get room type
        RoomTypeId roomTypeId = RoomTypeId.of(command.roomTypeId());
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
            .orElseThrow(() -> new ResourceNotFoundException("RoomType", command.roomTypeId()));
        
        // Create room
        Room room = Room.create(command.roomNumber(), roomType, command.amenities());
        
        // Add beds if specified
        if (command.numberOfBeds() != null && command.numberOfBeds() > 0) {
            for (int i = 1; i <= command.numberOfBeds(); i++) {
                room.addBed(i);
            }
        }
        
        return roomRepository.save(room);
    }
    
    public record CreateRoomCommand(
        Integer roomNumber,
        String roomTypeId,
        List<Amenity> amenities,
        Integer numberOfBeds
    ) {
        public CreateRoomCommand {
            Objects.requireNonNull(roomNumber, "Room number cannot be null");
            Objects.requireNonNull(roomTypeId, "Room type ID cannot be null");
        }
    }
}
