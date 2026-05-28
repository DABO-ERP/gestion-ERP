package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.AmenityDefinitionResponse;
import com.daboerp.gestion.api.dto.CreateAmenityDefinitionRequest;
import com.daboerp.gestion.application.usecase.room.CreateAmenityDefinitionUseCase;
import com.daboerp.gestion.application.usecase.room.DeleteAmenityDefinitionUseCase;
import com.daboerp.gestion.application.usecase.room.ListAmenityDefinitionsUseCase;
import com.daboerp.gestion.domain.entity.AmenityDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Amenity Definitions", description = "APIs for managing the amenity catalog")
public class AmenityDefinitionController {

    private final ListAmenityDefinitionsUseCase listAmenityDefinitionsUseCase;
    private final CreateAmenityDefinitionUseCase createAmenityDefinitionUseCase;
    private final DeleteAmenityDefinitionUseCase deleteAmenityDefinitionUseCase;

    public AmenityDefinitionController(ListAmenityDefinitionsUseCase listAmenityDefinitionsUseCase,
                                       CreateAmenityDefinitionUseCase createAmenityDefinitionUseCase,
                                       DeleteAmenityDefinitionUseCase deleteAmenityDefinitionUseCase) {
        this.listAmenityDefinitionsUseCase = listAmenityDefinitionsUseCase;
        this.createAmenityDefinitionUseCase = createAmenityDefinitionUseCase;
        this.deleteAmenityDefinitionUseCase = deleteAmenityDefinitionUseCase;
    }

    @GetMapping("/amenity-definitions")
    @Operation(summary = "List all amenity definitions", description = "Get all available amenities in the catalog")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    public ResponseEntity<List<AmenityDefinitionResponse>> listAmenityDefinitions() {
        List<AmenityDefinitionResponse> response = listAmenityDefinitionsUseCase.execute().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/amenity-definitions")
    @Operation(summary = "Create a new amenity definition", description = "Add a new amenity to the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Amenity definition created successfully",
            content = @Content(schema = @Schema(implementation = AmenityDefinitionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Amenity already exists with that name")
    })
    public ResponseEntity<AmenityDefinitionResponse> createAmenityDefinition(
            @Valid @RequestBody CreateAmenityDefinitionRequest request) {
        var command = new CreateAmenityDefinitionUseCase.CreateAmenityDefinitionCommand(
            request.name());
        AmenityDefinition definition = createAmenityDefinitionUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(definition));
    }

    @DeleteMapping("/amenity-definitions/{id}")
    @Operation(summary = "Delete an amenity definition", description = "Remove an amenity from the catalog and remove it from all rooms")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Amenity definition deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Amenity definition not found")
    })
    public ResponseEntity<Void> deleteAmenityDefinition(@PathVariable String id) {
        var command = new DeleteAmenityDefinitionUseCase.DeleteAmenityDefinitionCommand(id);
        deleteAmenityDefinitionUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }

    private AmenityDefinitionResponse toResponse(AmenityDefinition definition) {
        return new AmenityDefinitionResponse(
            definition.getId().getValue(),
            definition.getName()
        );
    }
}
