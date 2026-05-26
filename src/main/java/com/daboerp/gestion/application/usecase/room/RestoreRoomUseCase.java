package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.util.Objects;

public class RestoreRoomUseCase {

    private final RoomRepository roomRepository;

    public RestoreRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }

    public Room execute(RestoreRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomId roomId = RoomId.of(command.roomId());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room", command.roomId()));

        if (!room.isDeleted()) {
            throw new ResourceAlreadyExistsException("Room", "Room " + command.roomId() + " is already active");
        }

        room.setDeleted(false);
        room.markAsAvailable();

        return roomRepository.save(room);
    }

    public record RestoreRoomCommand(String roomId) {
        public RestoreRoomCommand {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
        }
    }
}
