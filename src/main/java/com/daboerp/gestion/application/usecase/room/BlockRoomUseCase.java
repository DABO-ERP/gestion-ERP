package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.time.LocalDate;
import java.util.Objects;

public class BlockRoomUseCase {

    private final RoomRepository roomRepository;
    private final RoomBlockRepository roomBlockRepository;

    public BlockRoomUseCase(RoomRepository roomRepository, RoomBlockRepository roomBlockRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
    }

    public RoomBlock execute(BlockRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        RoomId roomId = RoomId.of(command.roomId);
        roomRepository.findById(roomId)
            .orElseThrow(() -> new com.daboerp.gestion.application.exception.ResourceNotFoundException("Room", command.roomId));

        if (command.startDate.isAfter(command.endDate)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }

        RoomBlock block = RoomBlock.create(roomId, command.startDate, command.endDate, command.reason);
        return roomBlockRepository.save(block);
    }

    public record BlockRoomCommand(
        String roomId,
        LocalDate startDate,
        LocalDate endDate,
        String reason
    ) {
        public BlockRoomCommand {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
            Objects.requireNonNull(startDate, "Start date cannot be null");
            Objects.requireNonNull(endDate, "End date cannot be null");
        }
    }
}
