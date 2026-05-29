package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.RoomBlock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomBlockRepository {
    List<RoomBlock> findByRoomId(String roomId);
    List<RoomBlock> findOverlapping(String roomId, LocalDate start, LocalDate end);
    List<RoomBlock> findAllOverlappingRange(LocalDate start, LocalDate end);
    RoomBlock save(RoomBlock block);
    void deleteById(String id);
    Optional<RoomBlock> findById(String id);
}
