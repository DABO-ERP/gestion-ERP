package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetRoomBlocksUseCase {

    private final RoomBlockRepository roomBlockRepository;

    public GetRoomBlocksUseCase(RoomBlockRepository roomBlockRepository) {
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
    }

    public List<RoomBlock> execute(GetRoomBlocksQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        List<RoomBlock> blocks = roomBlockRepository.findByRoomId(query.roomId());

        if (query.activeOnly()) {
            LocalDate now = LocalDate.now();
            blocks = blocks.stream()
                .filter(block -> block.getEndDate().isAfter(now))
                .collect(Collectors.toList());
        }

        return blocks;
    }

    public record GetRoomBlocksQuery(
        String roomId,
        boolean activeOnly
    ) {
        public GetRoomBlocksQuery {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
        }
    }
}
