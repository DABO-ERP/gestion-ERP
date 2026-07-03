package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.RoomBlockJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for RoomBlock persistence.
 */
@Repository
public interface RoomBlockJpaRepository extends JpaRepository<RoomBlockJpaEntity, String> {

    List<RoomBlockJpaEntity> findByRoomIdOrderByStartDateAsc(String roomId);

    @Query("SELECT b FROM RoomBlockJpaEntity b WHERE b.roomId = :roomId AND b.startDate < :endDate AND b.endDate > :startDate")
    List<RoomBlockJpaEntity> findOverlapping(@Param("roomId") String roomId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM RoomBlockJpaEntity b WHERE b.startDate < :endDate AND b.endDate > :startDate")
    List<RoomBlockJpaEntity> findAllOverlappingRange(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);
}
