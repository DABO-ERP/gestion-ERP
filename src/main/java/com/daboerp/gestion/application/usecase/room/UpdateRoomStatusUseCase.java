package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.RoomStatus;

import java.util.Objects;

/**
 * Use case for updating the operational status of a room.
 */
public class UpdateRoomStatusUseCase {

    private final RoomRepository roomRepository;

    public UpdateRoomStatusUseCase(RoomRepository roomRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }

    public Room execute(UpdateRoomStatusCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomId roomId = RoomId.of(command.roomId());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room", command.roomId()));

        RoomStatus targetStatus = command.status();
        if (targetStatus == RoomStatus.AVAILABLE) {
            room.markAsAvailable();
        } else if (targetStatus == RoomStatus.OCCUPIED) {
            room.markAsOccupied();
        } else if (targetStatus == RoomStatus.OUT_OF_SERVICE) {
            room.markAsOutOfService();
        }

        return roomRepository.save(room);
    }

    public record UpdateRoomStatusCommand(
        String roomId,
        RoomStatus status
    ) {
        public UpdateRoomStatusCommand {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
            Objects.requireNonNull(status, "Status cannot be null");
        }
    }
}
