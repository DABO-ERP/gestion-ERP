package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Use case for creating a new room type.
 */
public class CreateRoomTypeUseCase {
    
    private final RoomTypeRepository roomTypeRepository;
    
    public CreateRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = Objects.requireNonNull(roomTypeRepository, "Room type repository cannot be null");
    }
    
    public RoomType execute(CreateRoomTypeCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        
        // Validate room type doesn't already exist
        if (roomTypeRepository.existsByName(command.name())) {
            throw new ResourceAlreadyExistsException("RoomType", command.name());
        }
        
        RoomType roomType = RoomType.create(
            command.name(),
            command.description(),
            command.maxOccupancy(),
            command.basePrice()
        );
        
        return roomTypeRepository.save(roomType);
    }
    
    public record CreateRoomTypeCommand(
        String name,
        String description,
        int maxOccupancy,
        BigDecimal basePrice
    ) {
        public CreateRoomTypeCommand {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(basePrice, "Base price cannot be null");
        }
    }
}
