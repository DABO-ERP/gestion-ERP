package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.util.Objects;

public class DeleteRoomUseCase {

    private final RoomRepository roomRepository;

    public DeleteRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }

    public void execute(DeleteRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomId roomId = RoomId.of(command.roomId());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room", command.roomId()));

        room.markAsDeleted();
        roomRepository.save(room);
    }

    public record DeleteRoomCommand(String roomId) {
        public DeleteRoomCommand {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
        }
    }
}
