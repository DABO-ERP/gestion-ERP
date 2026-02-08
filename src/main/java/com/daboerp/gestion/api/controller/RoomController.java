package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.*;
import com.daboerp.gestion.application.usecase.room.*;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.entity.RoomType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Room Management", description = "APIs for managing rooms, room types, and availability")
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
    @Operation(summary = "Create a new room type", description = "Define a new room type with pricing and capacity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Room type created successfully",
            content = @Content(schema = @Schema(implementation = RoomTypeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Room type already exists with this name")
    })
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
    @Operation(summary = "Create a new room", description = "Add a new room to the inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Room created successfully",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Room type not found"),
        @ApiResponse(responseCode = "409", description = "Room number already exists")
    })
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
    @Operation(summary = "List all rooms", description = "Get a complete list of all rooms in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    public ResponseEntity<List<RoomResponse>> listRooms() {
        List<Room> rooms = listRoomsUseCase.execute();
        List<RoomResponse> response = rooms.stream()
            .map(this::toRoomResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/rooms/available")
    @Operation(summary = "Find available rooms", description = "Search for rooms available in a specific date range with optional capacity filter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available rooms found")
    })
    public ResponseEntity<List<RoomResponse>> findAvailableRooms(
            @Parameter(description = "Check-in date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @Parameter(description = "Check-out date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @Parameter(description = "Minimum capacity required") @RequestParam(required = false) Integer minCapacity) {
        
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
