package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;

import java.util.Objects;

/**
 * Use case for removing a block from a room.
 */
public class UnblockRoomUseCase {

    private final RoomBlockRepository roomBlockRepository;

    public UnblockRoomUseCase(RoomBlockRepository roomBlockRepository) {
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
    }

    public void execute(UnblockRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomBlock block = roomBlockRepository.findById(command.blockId())
            .orElseThrow(() -> new ResourceNotFoundException("RoomBlock", command.blockId()));

        if (!block.getRoomId().equals(command.roomId())) {
            throw new ResourceNotFoundException("RoomBlock", command.blockId());
        }

        roomBlockRepository.deleteById(command.blockId());
    }

    public record UnblockRoomCommand(
        String roomId,
        String blockId
    ) {
        public UnblockRoomCommand {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
            Objects.requireNonNull(blockId, "Block ID cannot be null");
        }
    }
}
