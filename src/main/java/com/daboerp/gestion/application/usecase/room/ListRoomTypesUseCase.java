package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;

import java.util.List;
import java.util.Objects;

/**
 * Use case for listing all room types.
 */
public class ListRoomTypesUseCase {

    private final RoomTypeRepository roomTypeRepository;

    public ListRoomTypesUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = Objects.requireNonNull(roomTypeRepository, "RoomType repository cannot be null");
    }

    public List<RoomType> execute() {
        return roomTypeRepository.findAll();
    }
}
