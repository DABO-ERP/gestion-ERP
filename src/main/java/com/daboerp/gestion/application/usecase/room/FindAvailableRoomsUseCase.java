package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.RoomStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FindAvailableRoomsUseCase {

    private final RoomRepository roomRepository;
    private final RoomBlockRepository roomBlockRepository;

    public FindAvailableRoomsUseCase(RoomRepository roomRepository, RoomBlockRepository roomBlockRepository) {
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
        this.roomBlockRepository = Objects.requireNonNull(roomBlockRepository, "Room block repository cannot be null");
    }

    public List<Room> execute(FindAvailableRoomsQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");

        List<Room> candidateRooms;
        if (query.checkIn() != null && query.checkOut() != null) {
            if (query.minCapacity() != null && query.minCapacity() > 0) {
                candidateRooms = roomRepository.findAvailableByCapacity(
                    query.minCapacity(),
                    query.checkIn(),
                    query.checkOut()
                );
            } else {
                candidateRooms = roomRepository.findAvailableRooms(query.checkIn(), query.checkOut());
            }
            return filterBlockedRooms(candidateRooms, query.checkIn(), query.checkOut());
        }

        List<Room> available = roomRepository.findByStatus(RoomStatus.AVAILABLE);
        if (query.minCapacity() != null && query.minCapacity() > 0) {
            int minCapacity = query.minCapacity();
            available = available.stream()
                .filter(room -> room.getRoomType().getMaxOccupancy() >= minCapacity)
                .collect(Collectors.toList());
        }
        if (query.checkIn() != null && query.checkOut() != null) {
            return filterBlockedRooms(available, query.checkIn(), query.checkOut());
        }
        return filterBlockedRooms(available, LocalDate.now(), LocalDate.now().plusDays(1));
    }

    private List<Room> filterBlockedRooms(List<Room> rooms, LocalDate checkIn, LocalDate checkOut) {
        List<String> blockedRoomIds = roomBlockRepository.findAllActive().stream()
            .filter(block -> block.overlaps(checkIn, checkOut))
            .map(block -> block.getRoomId().getValue())
            .distinct()
            .toList();

        if (blockedRoomIds.isEmpty()) {
            return rooms;
        }

        return rooms.stream()
            .filter(room -> !blockedRoomIds.contains(room.getId().getValue()))
            .collect(Collectors.toList());
    }

    public record FindAvailableRoomsQuery(
        LocalDate checkIn,
        LocalDate checkOut,
        Integer minCapacity
    ) {}
}
