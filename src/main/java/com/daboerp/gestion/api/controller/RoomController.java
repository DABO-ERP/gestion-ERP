package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.*;
import com.daboerp.gestion.application.usecase.room.*;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.RoomStatus;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Room Management", description = "APIs for managing rooms, room types, and availability")
public class RoomController {

    private final CreateRoomTypeUseCase createRoomTypeUseCase;
    private final CreateRoomUseCase createRoomUseCase;
    private final ListRoomsUseCase listRoomsUseCase;
    private final ListRoomTypesUseCase listRoomTypesUseCase;
    private final FindAvailableRoomsUseCase findAvailableRoomsUseCase;
    private final UpdateRoomStatusUseCase updateRoomStatusUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;
    private final DeleteRoomUseCase deleteRoomUseCase;
    private final UpdateRoomTypeUseCase updateRoomTypeUseCase;
    private final DeleteRoomTypeUseCase deleteRoomTypeUseCase;
    private final ReactivateRoomUseCase reactivateRoomUseCase;
    private final RestoreRoomUseCase restoreRoomUseCase;
    private final BlockRoomUseCase blockRoomUseCase;
    private final UnblockRoomUseCase unblockRoomUseCase;
    private final GetRoomBlocksUseCase getRoomBlocksUseCase;

    public RoomController(CreateRoomTypeUseCase createRoomTypeUseCase,
                         CreateRoomUseCase createRoomUseCase,
                         ListRoomsUseCase listRoomsUseCase,
                         ListRoomTypesUseCase listRoomTypesUseCase,
                         FindAvailableRoomsUseCase findAvailableRoomsUseCase,
                         UpdateRoomStatusUseCase updateRoomStatusUseCase,
                         UpdateRoomUseCase updateRoomUseCase,
                         DeleteRoomUseCase deleteRoomUseCase,
                         UpdateRoomTypeUseCase updateRoomTypeUseCase,
                         DeleteRoomTypeUseCase deleteRoomTypeUseCase,
                         ReactivateRoomUseCase reactivateRoomUseCase,
                         RestoreRoomUseCase restoreRoomUseCase,
                         BlockRoomUseCase blockRoomUseCase,
                         UnblockRoomUseCase unblockRoomUseCase,
                         GetRoomBlocksUseCase getRoomBlocksUseCase) {
        this.createRoomTypeUseCase = createRoomTypeUseCase;
        this.createRoomUseCase = createRoomUseCase;
        this.listRoomsUseCase = listRoomsUseCase;
        this.listRoomTypesUseCase = listRoomTypesUseCase;
        this.findAvailableRoomsUseCase = findAvailableRoomsUseCase;
        this.updateRoomStatusUseCase = updateRoomStatusUseCase;
        this.updateRoomUseCase = updateRoomUseCase;
        this.deleteRoomUseCase = deleteRoomUseCase;
        this.updateRoomTypeUseCase = updateRoomTypeUseCase;
        this.deleteRoomTypeUseCase = deleteRoomTypeUseCase;
        this.reactivateRoomUseCase = reactivateRoomUseCase;
        this.restoreRoomUseCase = restoreRoomUseCase;
        this.blockRoomUseCase = blockRoomUseCase;
        this.unblockRoomUseCase = unblockRoomUseCase;
        this.getRoomBlocksUseCase = getRoomBlocksUseCase;
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

    @GetMapping("/room-types")
    @Operation(summary = "List all room types", description = "Get all available room types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    public ResponseEntity<List<RoomTypeResponse>> listRoomTypes() {
        List<RoomTypeResponse> response = listRoomTypesUseCase.execute().stream()
            .map(this::toRoomTypeResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/room-types/{id}")
    @Operation(summary = "Update a room type", description = "Update room type details and pricing")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room type updated successfully",
            content = @Content(schema = @Schema(implementation = RoomTypeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Room type not found"),
        @ApiResponse(responseCode = "409", description = "Room type name already exists")
    })
    public ResponseEntity<RoomTypeResponse> updateRoomType(
            @Parameter(description = "Room Type ID") @PathVariable String id,
            @Valid @RequestBody UpdateRoomTypeRequest request) {
        var command = new UpdateRoomTypeUseCase.UpdateRoomTypeCommand(
            id,
            request.name(),
            request.description(),
            request.maxOccupancy(),
            request.basePrice()
        );
        RoomType roomType = updateRoomTypeUseCase.execute(command);
        return ResponseEntity.ok(toRoomTypeResponse(roomType));
    }

    @DeleteMapping("/room-types/{id}")
    @Operation(summary = "Delete a room type", description = "Delete a room type by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Room type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Room type not found")
    })
    public ResponseEntity<Void> deleteRoomType(
            @Parameter(description = "Room Type ID") @PathVariable String id) {
        var command = new DeleteRoomTypeUseCase.DeleteRoomTypeCommand(id);
        deleteRoomTypeUseCase.execute(command);
        return ResponseEntity.noContent().build();
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
            request.amenities() != null ? request.amenities().stream().map(Amenity::of).collect(Collectors.toList()) : null,
            request.numberOfBeds(),
            request.imageUrls()
        );
        Room room = createRoomUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toRoomResponse(room));
    }

    @GetMapping("/rooms")
    @Operation(summary = "List all rooms", description = "Get a complete list of all rooms in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    public ResponseEntity<List<RoomResponse>> listRooms(@RequestParam(value = "status", required = false) String status) {
        List<RoomResponse> response = (Objects.isNull(status) || status.isBlank()
                ? listRoomsUseCase.execute()
                : listRoomsUseCase.execute(parseRoomStatus(status)))
                .stream()
            .map(this::toRoomResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/rooms/{id}")
    @Operation(summary = "Update room status", description = "Update the operational status of a room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room status updated successfully",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<RoomResponse> updateRoomStatus(
            @Parameter(description = "Room ID") @PathVariable String id,
            @Valid @RequestBody UpdateRoomStatusRequest request) {
        RoomStatus status = RoomStatus.valueOf(request.status());
        var command = new UpdateRoomStatusUseCase.UpdateRoomStatusCommand(id, status);
        Room updated = updateRoomStatusUseCase.execute(command);
        return ResponseEntity.ok(toRoomResponse(updated));
    }

    @PutMapping("/rooms/{id}/details")
    @Operation(summary = "Update room details", description = "Update room number, type, amenities, and beds")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room updated successfully",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Room or room type not found")
    })
    public ResponseEntity<RoomResponse> updateRoomDetails(
            @Parameter(description = "Room ID") @PathVariable String id,
            @Valid @RequestBody UpdateRoomRequest request) {
        var command = new UpdateRoomUseCase.UpdateRoomCommand(
            id,
            request.roomNumber(),
            request.roomTypeId(),
            request.amenities() != null ? request.amenities().stream().map(Amenity::of).collect(Collectors.toList()) : null,
            request.numberOfBeds(),
            request.imageUrls()
        );
        Room updated = updateRoomUseCase.execute(command);
        return ResponseEntity.ok(toRoomResponse(updated));
    }

    @DeleteMapping("/rooms/{id}")
    @Operation(summary = "Delete room (soft delete)", description = "Mark a room as deleted without removing it from the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Room deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<Void> deleteRoom(@Parameter(description = "Room ID") @PathVariable String id) {
        var command = new DeleteRoomUseCase.DeleteRoomCommand(id);
        deleteRoomUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/rooms/{id}/reactivate")
    @Operation(summary = "Reactivate a deleted room", description = "Restore a previously deleted room with updated details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room reactivated successfully",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "404", description = "Room not found"),
        @ApiResponse(responseCode = "409", description = "Room number already in use by another active room")
    })
    public ResponseEntity<RoomResponse> reactivateRoom(
            @Parameter(description = "Room ID") @PathVariable String id,
            @Valid @RequestBody UpdateRoomRequest request) {
        var command = new ReactivateRoomUseCase.ReactivateRoomCommand(
            id,
            request.roomNumber(),
            request.roomTypeId(),
            request.amenities() != null ? request.amenities().stream().map(Amenity::of).collect(Collectors.toList()) : null,
            request.numberOfBeds(),
            request.imageUrls()
        );
        Room updated = reactivateRoomUseCase.execute(command);
        return ResponseEntity.ok(toRoomResponse(updated));
    }

    @PutMapping("/rooms/{id}/restore")
    @Operation(summary = "Restore a deleted room with its original data", description = "Reactivate a room keeping its previous values")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room restored successfully",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "404", description = "Room not found"),
        @ApiResponse(responseCode = "409", description = "Room is already active")
    })
    public ResponseEntity<RoomResponse> restoreRoom(
            @Parameter(description = "Room ID") @PathVariable String id) {
        var command = new RestoreRoomUseCase.RestoreRoomCommand(id);
        Room updated = restoreRoomUseCase.execute(command);
        return ResponseEntity.ok(toRoomResponse(updated));
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
        var query = new FindAvailableRoomsUseCase.FindAvailableRoomsQuery(checkIn, checkOut, minCapacity);
        List<Room> rooms = findAvailableRoomsUseCase.execute(query);
        List<RoomResponse> response = rooms.stream()
            .map(this::toRoomResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rooms/{id}/blocks")
    @Operation(summary = "Block a room for a date range", description = "Create an unavailability block for a room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Room blocked successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<RoomBlockResponse> blockRoom(
            @Parameter(description = "Room ID") @PathVariable String id,
            @Valid @RequestBody BlockRoomRequest request) {
        var command = new BlockRoomUseCase.BlockRoomCommand(id, request.startDate(), request.endDate(), request.reason());
        RoomBlock block = blockRoomUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toRoomBlockResponse(block));
    }

    @GetMapping("/rooms/{id}/blocks")
    @Operation(summary = "Get room blocks", description = "Get all blocks for a specific room")
    public ResponseEntity<List<RoomBlockResponse>> getRoomBlocks(
            @Parameter(description = "Room ID") @PathVariable String id,
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        var query = new GetRoomBlocksUseCase.GetRoomBlocksQuery(id, activeOnly);
        List<RoomBlockResponse> response = getRoomBlocksUseCase.execute(query).stream()
            .map(this::toRoomBlockResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rooms/{roomId}/blocks/{blockId}")
    @Operation(summary = "Unblock a room", description = "Remove or deactivate a room block")
    public ResponseEntity<Void> unblockRoom(
            @Parameter(description = "Room ID") @PathVariable String roomId,
            @Parameter(description = "Block ID") @PathVariable String blockId) {
        var command = new UnblockRoomUseCase.UnblockRoomCommand(blockId);
        unblockRoomUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rooms/{id}/is-available")
    @Operation(summary = "Check if room is available in a date range")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @Parameter(description = "Room ID") @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        var query = new FindAvailableRoomsUseCase.FindAvailableRoomsQuery(checkIn, checkOut, null);
        boolean available = findAvailableRoomsUseCase.execute(query).stream()
            .anyMatch(room -> room.getId().getValue().equals(id));
        return ResponseEntity.ok(available);
    }

    private RoomStatus parseRoomStatus(String status) {
        try {
            return RoomStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room status: " + status);
        }
    }

    private RoomResponse toRoomResponse(Room room) {
        return new RoomResponse(
            room.getId().getValue(),
            room.getRoomNumber(),
            toRoomTypeResponse(room.getRoomType()),
            room.getRoomStatus(),
            room.getAmenities().stream().map(Amenity::getValue).collect(Collectors.toList()),
            room.getBeds().size(),
            room.getImageUrls(),
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

    private RoomBlockResponse toRoomBlockResponse(RoomBlock block) {
        return new RoomBlockResponse(
            block.getId().getValue(),
            block.getRoomId().getValue(),
            null,
            block.getStartDate(),
            block.getEndDate(),
            block.getReason(),
            block.isActive(),
            block.getCreatedAt()
        );
    }
}
