package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;
import com.daboerp.gestion.domain.valueobject.RoomBlockId;

import java.util.Objects;

public class UnblockRoomUseCase {

    private final RoomBlockRepository roomBlockRepository;

    public UnblockRoomUseCase(RoomBlockRepository roomBlockRepository) {
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
    }

    public void execute(UnblockRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        RoomBlockId blockId = RoomBlockId.of(command.blockId);
        RoomBlock block = roomBlockRepository.findById(blockId)
            .orElseThrow(() -> new ResourceNotFoundException("RoomBlock", command.blockId));

        block.deactivate();
        roomBlockRepository.save(block);
    }

    public record UnblockRoomCommand(String blockId) {
        public UnblockRoomCommand {
            Objects.requireNonNull(blockId, "Block ID cannot be null");
        }
    }
}
