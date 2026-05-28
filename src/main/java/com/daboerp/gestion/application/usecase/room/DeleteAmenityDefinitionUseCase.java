package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.AmenityDefinitionRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.AmenityDefinitionId;
import com.daboerp.gestion.domain.valueobject.Amenity;

import java.util.List;
import java.util.Objects;

public class DeleteAmenityDefinitionUseCase {

    private final AmenityDefinitionRepository amenityDefinitionRepository;
    private final RoomRepository roomRepository;

    public DeleteAmenityDefinitionUseCase(AmenityDefinitionRepository amenityDefinitionRepository,
                                         RoomRepository roomRepository) {
        this.amenityDefinitionRepository = Objects.requireNonNull(amenityDefinitionRepository);
        this.roomRepository = Objects.requireNonNull(roomRepository);
    }

    public DeleteResult execute(DeleteAmenityDefinitionCommand command) {
        Objects.requireNonNull(command);
        AmenityDefinitionId id = AmenityDefinitionId.of(command.definitionId);

        var definition = amenityDefinitionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AmenityDefinition", command.definitionId));

        String amenityName = definition.getName();
        Amenity amenity = Amenity.of(amenityName);

        List<Room> affectedRooms = roomRepository.findAllActive().stream()
            .filter(room -> room.getAmenities().contains(amenity))
            .toList();

        for (Room room : affectedRooms) {
            room.removeAmenity(amenity);
            roomRepository.save(room);
        }

        amenityDefinitionRepository.delete(id);

        return new DeleteResult(affectedRooms.size());
    }

    public record DeleteAmenityDefinitionCommand(String definitionId) {
        public DeleteAmenityDefinitionCommand {
            Objects.requireNonNull(definitionId, "Definition ID cannot be null");
        }
    }

    public record DeleteResult(int affectedRoomsCount) {}
}
