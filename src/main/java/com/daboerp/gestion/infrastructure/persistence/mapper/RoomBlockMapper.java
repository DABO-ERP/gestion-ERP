package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.infrastructure.persistence.entity.RoomBlockJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomBlockMapper {

    public RoomBlock toDomain(RoomBlockJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new RoomBlock(
            entity.getId(),
            entity.getRoomId(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getReason(),
            entity.getCreatedAt()
        );
    }

    public RoomBlockJpaEntity toJpa(RoomBlock domain) {
        RoomBlockJpaEntity entity = new RoomBlockJpaEntity();
        entity.setId(domain.getId());
        entity.setRoomId(domain.getRoomId());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setReason(domain.getReason());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
