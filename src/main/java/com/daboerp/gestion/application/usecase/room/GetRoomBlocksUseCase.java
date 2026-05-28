package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;
import com.daboerp.gestion.domain.valueobject.RoomBlockId;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.util.List;
import java.util.Objects;

public class GetRoomBlocksUseCase {

    private final RoomBlockRepository roomBlockRepository;

    public GetRoomBlocksUseCase(RoomBlockRepository roomBlockRepository) {
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
    }

    public List<RoomBlock> execute(GetRoomBlocksQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        RoomId roomId = RoomId.of(query.roomId);
        if (query.activeOnly) {
            return roomBlockRepository.findActiveByRoomId(roomId);
        }
        return roomBlockRepository.findByRoomId(roomId);
    }

    public record GetRoomBlocksQuery(String roomId, boolean activeOnly) {
        public GetRoomBlocksQuery {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
        }
    }
}
