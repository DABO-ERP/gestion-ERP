package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.valueobject.RoomBlockId;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.infrastructure.persistence.entity.RoomBlockJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomBlockMapper {

    public RoomBlockJpaEntity toJpaEntity(RoomBlock block) {
        RoomBlockJpaEntity entity = new RoomBlockJpaEntity();
        entity.setId(block.getId().getValue());
        entity.setRoomId(block.getRoomId().getValue());
        entity.setStartDate(block.getStartDate());
        entity.setEndDate(block.getEndDate());
        entity.setReason(block.getReason());
        entity.setActive(block.isActive());
        entity.setCreatedAt(block.getCreatedAt());
        return entity;
    }

    public RoomBlock toDomainEntity(RoomBlockJpaEntity entity) {
        if (entity == null) return null;
        return RoomBlock.reconstitute(
            RoomBlockId.of(entity.getId()),
            RoomId.of(entity.getRoomId()),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getReason(),
            entity.isActive(),
            entity.getCreatedAt()
        );
    }
}
