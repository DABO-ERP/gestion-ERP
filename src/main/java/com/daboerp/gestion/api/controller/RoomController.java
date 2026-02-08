package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.*;
import com.daboerp.gestion.application.usecase.room.*;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.entity.RoomType;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for room management.
 */
@RestController
@RequestMapping("/api/v1")
public class RoomController {
    
    private final CreateRoomTypeUseCase createRoomTypeUseCase;
    private final CreateRoomUseCase createRoomUseCase;
    private final ListRoomsUseCase listRoomsUseCase;
    private final FindAvailableRoomsUseCase findAvailableRoomsUseCase;
    
    public RoomController(CreateRoomTypeUseCase createRoomTypeUseCase,
                         CreateRoomUseCase createRoomUseCase,
                         ListRoomsUseCase listRoomsUseCase,
                         FindAvailableRoomsUseCase findAvailableRoomsUseCase) {
        this.createRoomTypeUseCase = createRoomTypeUseCase;
        this.createRoomUseCase = createRoomUseCase;
        this.listRoomsUseCase = listRoomsUseCase;
        this.findAvailableRoomsUseCase = findAvailableRoomsUseCase;
    }
    
    @PostMapping("/room-types")
    public ResponseEntity<RoomTypeResponse> createRoomType(@Valid @RequestBody CreateRoomTypeRequest request) {
        var command = new CreateRoomTypeUseCase.CreateRoomTypeCommand(
            request.name(),
            request.description(),
            request.maxOccupancy(),
            request.basePrice()
        );
        
        RoomType roomType = createRoomTypeUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toRoomTypeResponse(roomType));
    }
    
    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        var command = new CreateRoomUseCase.CreateRoomCommand(
            request.roomNumber(),
            request.roomTypeId(),
            request.amenities(),
            request.numberOfBeds()
        );
        
        Room room = createRoomUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toRoomResponse(room));
    }
    
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> listRooms() {
        List<Room> rooms = listRoomsUseCase.execute();
        List<RoomResponse> response = rooms.stream()
            .map(this::toRoomResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/rooms/available")
    public ResponseEntity<List<RoomResponse>> findAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) Integer minCapacity) {
        
        var query = new FindAvailableRoomsUseCase.FindAvailableRoomsQuery(
            checkIn,
            checkOut,
            minCapacity
        );
        
        List<Room> rooms = findAvailableRoomsUseCase.execute(query);
        List<RoomResponse> response = rooms.stream()
            .map(this::toRoomResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    private RoomResponse toRoomResponse(Room room) {
        return new RoomResponse(
            room.getId().getValue(),
            room.getRoomNumber(),
            toRoomTypeResponse(room.getRoomType()),
            room.getRoomStatus(),
            room.getAmenities(),
            room.getBeds().size(),
            room.getCreatedAt()
        );
    }
    
    private RoomTypeResponse toRoomTypeResponse(RoomType roomType) {
        return new RoomTypeResponse(
            roomType.getId().getValue(),
            roomType.getName(),
            roomType.getDescription(),
            roomType.getMaxOccupancy(),
            roomType.getBasePrice()
        );
    }
}
