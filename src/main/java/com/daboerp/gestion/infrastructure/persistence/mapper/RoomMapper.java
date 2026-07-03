package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.*;
import com.daboerp.gestion.domain.valueobject.*;
import com.daboerp.gestion.infrastructure.persistence.entity.BedJpaEntity;
import com.daboerp.gestion.infrastructure.persistence.entity.RoomJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mapper between Room domain entity and JPA entity.
 * Handles conversion between domain and infrastructure layers.
 */
@Component
public class RoomMapper {

    public RoomJpaEntity toJpaEntity(Room room) {
        RoomJpaEntity entity = new RoomJpaEntity();
        entity.setId(room.getId().getValue());
        entity.setRoomNumber(room.getRoomNumber());
        entity.setRoomStatus(room.getRoomStatus());
        entity.setCreatedAt(room.getCreatedAt());

        RoomType roomType = room.getRoomType();
        entity.setRoomTypeId(roomType.getId().getValue());
        entity.setRoomTypeName(roomType.getName());
        entity.setRoomTypeDescription(roomType.getDescription());
        entity.setRoomTypeMaxOccupancy(roomType.getMaxOccupancy());
        entity.setRoomTypeBasePrice(roomType.getBasePrice());

        entity.setAmenities(room.getAmenities().stream().map(Amenity::getValue).collect(Collectors.toList()));
        entity.setImageUrls(room.getImageUrls());

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

        entity.setDeleted(room.isDeleted());

        return entity;
    }

    public Room toDomainEntity(RoomJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        RoomType roomType = RoomType.reconstitute(
            RoomTypeId.of(entity.getRoomTypeId()),
            entity.getRoomTypeName(),
            entity.getRoomTypeDescription(),
            entity.getRoomTypeMaxOccupancy(),
            entity.getRoomTypeBasePrice()
        );

        List<Bed> beds = (entity.getBeds() != null) ?
            entity.getBeds().stream()
                .map(bedEntity -> Bed.reconstitute(
                    BedId.of(bedEntity.getId()),
                    bedEntity.getBedNumber(),
                    null
                ))
                .collect(Collectors.toList())
            : List.of();

        return Room.reconstitute(
            RoomId.of(entity.getId()),
            entity.getRoomNumber(),
            roomType,
            entity.getRoomStatus(),
            entity.getAmenities() != null ? entity.getAmenities().stream().map(Amenity::of).collect(Collectors.toList()) : List.of(),
            beds,
            entity.getCreatedAt(),
            entity.getImageUrls() != null ? entity.getImageUrls() : List.of(),
            entity.isDeleted()
        );
    }
}
