package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.RoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for RoomType persistence.
 * Simplified version - room types are embedded in rooms.
 */
@Repository
public interface RoomTypeJpaRepository extends JpaRepository<RoomJpaEntity, String> {
    
    Optional<RoomJpaEntity> findByRoomTypeName(String name);
    
    boolean existsByRoomTypeName(String name);
    
    List<RoomJpaEntity> findDistinctByRoomTypeId(String roomTypeId);
}
