package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.RoomTypeId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReactivateRoomUseCase {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public ReactivateRoomUseCase(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
        this.roomTypeRepository = Objects.requireNonNull(roomTypeRepository, "Room type repository cannot be null");
    }

    public Room execute(ReactivateRoomCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        RoomId roomId = RoomId.of(command.roomId());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room", command.roomId()));

        if (!room.isDeleted()) {
            throw new ResourceAlreadyExistsException("Room", "Room " + command.roomId() + " is already active");
        }

        if (command.roomNumber() != null) {
            roomRepository.findByRoomNumber(command.roomNumber())
                .filter(r -> !r.getId().equals(roomId))
                .ifPresent(r -> { throw new ResourceAlreadyExistsException("Room", command.roomNumber().toString()); });
            room.setRoomNumber(command.roomNumber());
        }

        if (command.roomTypeId() != null) {
            RoomTypeId roomTypeId = RoomTypeId.of(command.roomTypeId());
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", command.roomTypeId()));
            room.updateRoomType(roomType);
        }

        room.setDeleted(false);
        room.markAsAvailable();

        if (command.amenities() != null) {
            new ArrayList<>(room.getAmenities()).forEach(a -> room.removeAmenity(a));
            command.amenities().forEach(room::addAmenity);
        }

        if (command.numberOfBeds() != null && command.numberOfBeds() > 0) {
            int currentBeds = room.getBeds().size();
            if (command.numberOfBeds() > currentBeds) {
                for (int i = currentBeds + 1; i <= command.numberOfBeds(); i++) {
                    room.addBed(i);
                }
            } else if (command.numberOfBeds() < currentBeds) {
                for (int i = currentBeds; i > command.numberOfBeds(); i--) {
                    room.removeBed(i);
                }
            }
        }

        if (command.imageUrls() != null) {
            new ArrayList<>(room.getImageUrls()).forEach(room::removeImageUrl);
            command.imageUrls().forEach(room::addImageUrl);
        }

        return roomRepository.save(room);
    }

    public record ReactivateRoomCommand(
        String roomId,
        Integer roomNumber,
        String roomTypeId,
        List<Amenity> amenities,
        Integer numberOfBeds,
        List<String> imageUrls
    ) {
        public ReactivateRoomCommand {
            Objects.requireNonNull(roomId, "Room ID cannot be null");
        }
    }
}
