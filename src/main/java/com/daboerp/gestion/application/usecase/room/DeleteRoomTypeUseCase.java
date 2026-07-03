package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import com.daboerp.gestion.domain.valueobject.RoomTypeId;

import java.util.Objects;

/**
 * Use case for deleting a room type.
 */
public class DeleteRoomTypeUseCase {

    private final RoomTypeRepository roomTypeRepository;

    public DeleteRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = Objects.requireNonNull(roomTypeRepository, "Room type repository cannot be null");
    }

    public void execute(DeleteRoomTypeCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomTypeId roomTypeId = RoomTypeId.of(command.roomTypeId());
        roomTypeRepository.findById(roomTypeId)
            .orElseThrow(() -> new ResourceNotFoundException("RoomType", command.roomTypeId()));

        roomTypeRepository.delete(roomTypeId);
    }

    public record DeleteRoomTypeCommand(String roomTypeId) {
        public DeleteRoomTypeCommand {
            Objects.requireNonNull(roomTypeId, "Room type ID cannot be null");
        }
    }
}
