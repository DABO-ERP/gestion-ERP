package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.RoomBlockJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomBlockJpaRepository extends JpaRepository<RoomBlockJpaEntity, String> {

    List<RoomBlockJpaEntity> findByRoomIdOrderByStartDateAsc(String roomId);

    List<RoomBlockJpaEntity> findByRoomIdAndActiveTrueOrderByStartDateAsc(String roomId);

    @Query("SELECT b FROM RoomBlockJpaEntity b WHERE b.roomId = :roomId AND b.active = true " +
           "AND NOT (b.endDate < :startDate OR b.startDate > :endDate)")
    List<RoomBlockJpaEntity> findActiveByRoomIdInDateRange(@Param("roomId") String roomId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM RoomBlockJpaEntity b WHERE b.active = true " +
           "AND b.startDate <= :date AND b.endDate >= :date")
    List<RoomBlockJpaEntity> findActiveBlocksOnDate(@Param("date") LocalDate date);

    List<RoomBlockJpaEntity> findByActiveTrueOrderByStartDateAsc();
}
