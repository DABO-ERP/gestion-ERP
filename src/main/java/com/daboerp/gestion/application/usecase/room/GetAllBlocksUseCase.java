package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class GetAllBlocksUseCase {

    private final RoomBlockRepository roomBlockRepository;

    public GetAllBlocksUseCase(RoomBlockRepository roomBlockRepository) {
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
    }

    public List<RoomBlock> execute(LocalDate startDate, LocalDate endDate) {
        return roomBlockRepository.findAllOverlappingRange(startDate, endDate);
    }
}
