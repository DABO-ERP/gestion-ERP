package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class BlockRoomUseCase {

    private final RoomBlockRepository roomBlockRepository;
    private final RoomRepository roomRepository;

    public BlockRoomUseCase(RoomBlockRepository roomBlockRepository, RoomRepository roomRepository) {
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }

    public RoomBlock execute(BlockRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomId roomId = RoomId.of(command.roomId());
        roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room", command.roomId()));

        RoomBlock block = new RoomBlock(
            UUID.randomUUID().toString(),
            command.roomId(),
            command.startDate(),
            command.endDate(),
            command.reason(),
            LocalDateTime.now()
        );

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
