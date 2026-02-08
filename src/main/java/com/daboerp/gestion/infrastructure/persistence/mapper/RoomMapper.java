package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.*;
import com.daboerp.gestion.domain.valueobject.*;
import com.daboerp.gestion.infrastructure.persistence.entity.RoomJpaEntity;
import com.daboerp.gestion.infrastructure.persistence.entity.BedJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper between Room domain entity and JPA entity.
 */
@Component
public class RoomMapper {
    
    public RoomJpaEntity toJpaEntity(Room room) {
        RoomJpaEntity entity = new RoomJpaEntity();
        entity.setId(room.getId().getValue());
        entity.setRoomNumber(room.getRoomNumber());
        entity.setRoomStatus(room.getRoomStatus());
        entity.setCreatedAt(room.getCreatedAt());
        
        // Map room type
        RoomType roomType = room.getRoomType();
        entity.setRoomTypeId(roomType.getId().getValue());
        entity.setRoomTypeName(roomType.getName());
        entity.setRoomTypeDescription(roomType.getDescription());
        entity.setRoomTypeMaxOccupancy(roomType.getMaxOccupancy());
        entity.setRoomTypeBasePrice(roomType.getBasePrice());
        
        // Map amenities
        entity.setAmenities(room.getAmenities());
        
        // Map beds
        List<BedJpaEntity> bedEntities = room.getBeds().stream()
            .map(bed -> {
                BedJpaEntity bedEntity = new BedJpaEntity();
                bedEntity.setId(bed.getId().getValue());
                bedEntity.setBedNumber(bed.getBedNumber());
                bedEntity.setRoomId(room.getId().getValue());
                return bedEntity;
            })
            .collect(Collectors.toList());
        entity.setBeds(bedEntities);
        
        return entity;
    }
    
    public Room toDomainEntity(RoomJpaEntity entity) {
        // Reconstitute room type
        RoomType roomType = RoomType.reconstitute(
            RoomTypeId.of(entity.getRoomTypeId()),
            entity.getRoomTypeName(),
            entity.getRoomTypeDescription(),
            entity.getRoomTypeMaxOccupancy(),
            entity.getRoomTypeBasePrice()
        );
        
        // Reconstitute beds (will be added to room after creation)
        List<Bed> beds = entity.getBeds().stream()
            .map(bedEntity -> Bed.reconstitute(
                BedId.of(bedEntity.getId()),
                bedEntity.getBedNumber(),
                null // Room reference will be set during reconstitution
            ))
            .collect(Collectors.toList());
        
        // Reconstitute room
        return Room.reconstitute(
            RoomId.of(entity.getId()),
            entity.getRoomNumber(),
            roomType,
            entity.getRoomStatus(),
            entity.getAmenities(),
            beds,
            entity.getCreatedAt()
        );
    }
}
