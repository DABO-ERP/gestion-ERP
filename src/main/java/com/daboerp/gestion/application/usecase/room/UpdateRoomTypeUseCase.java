package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import com.daboerp.gestion.domain.valueobject.RoomTypeId;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Use case for updating an existing room type.
 */
public class UpdateRoomTypeUseCase {

    private final RoomTypeRepository roomTypeRepository;

    public UpdateRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = Objects.requireNonNull(roomTypeRepository, "Room type repository cannot be null");
    }

    public RoomType execute(UpdateRoomTypeCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomTypeId roomTypeId = RoomTypeId.of(command.roomTypeId());
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
            .orElseThrow(() -> new ResourceNotFoundException("RoomType", command.roomTypeId()));

        if (command.name() != null && !command.name().isBlank()) {
            roomTypeRepository.findByName(command.name())
                .filter(existing -> !existing.getId().equals(roomTypeId))
                .ifPresent(t -> { throw new ResourceAlreadyExistsException("RoomType", command.name()); });
            roomType.updateDetails(command.name(), command.description(), command.maxOccupancy());
        }

        if (command.basePrice() != null) {
            roomType.updatePricing(command.basePrice());
        }

        return roomTypeRepository.save(roomType);
    }

    public record UpdateRoomTypeCommand(
        String roomTypeId,
        String name,
        String description,
        Integer maxOccupancy,
        BigDecimal basePrice
    ) {
        public UpdateRoomTypeCommand {
            Objects.requireNonNull(roomTypeId, "Room type ID cannot be null");
        }
    }
}
