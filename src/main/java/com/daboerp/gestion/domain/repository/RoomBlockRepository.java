package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.valueobject.RoomBlockId;
import com.daboerp.gestion.domain.valueobject.RoomId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomBlockRepository {

    RoomBlock save(RoomBlock block);

    Optional<RoomBlock> findById(RoomBlockId id);

    List<RoomBlock> findByRoomId(RoomId roomId);

    List<RoomBlock> findActiveByRoomId(RoomId roomId);

    List<RoomBlock> findActiveByRoomIdInDateRange(RoomId roomId, LocalDate startDate, LocalDate endDate);

    List<RoomBlock> findActiveBlocksIntersectingDate(LocalDate date);

    List<RoomBlock> findAllActive();

    void delete(RoomBlockId id);
}
