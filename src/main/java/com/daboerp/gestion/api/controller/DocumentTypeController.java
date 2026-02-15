package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.CreateDocumentTypeRequest;
import com.daboerp.gestion.api.dto.DocumentTypeResponse;
import com.daboerp.gestion.api.dto.PaginatedResponse;
import com.daboerp.gestion.application.command.documenttype.CreateDocumentTypeCommand;
import com.daboerp.gestion.application.usecase.documenttype.*;
import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

/**
 * REST controller for document type management.
 */
@RestController
@RequestMapping("/api/v1/document-types")
@Tag(name = "Document Type Management", description = "APIs for managing document types and validation rules")
public class DocumentTypeController {
    
    private final ListDocumentTypesUseCase listDocumentTypesUseCase;
    private final GetDocumentTypeUseCase getDocumentTypeUseCase;
    private final CreateDocumentTypeUseCase createDocumentTypeUseCase;
    private final SearchDocumentTypesUseCase searchDocumentTypesUseCase;
    private final ListDocumentTypesWithPaginationUseCase listWithPaginationUseCase;
    
    public DocumentTypeController(ListDocumentTypesUseCase listDocumentTypesUseCase,
                                 GetDocumentTypeUseCase getDocumentTypeUseCase,
                                 CreateDocumentTypeUseCase createDocumentTypeUseCase,
                                 SearchDocumentTypesUseCase searchDocumentTypesUseCase,
                                 ListDocumentTypesWithPaginationUseCase listWithPaginationUseCase) {
        this.listDocumentTypesUseCase = listDocumentTypesUseCase;
        this.getDocumentTypeUseCase = getDocumentTypeUseCase;
        this.createDocumentTypeUseCase = createDocumentTypeUseCase;
        this.searchDocumentTypesUseCase = searchDocumentTypesUseCase;
        this.listWithPaginationUseCase = listWithPaginationUseCase;
    }
    
    @GetMapping
    @Operation(summary = "List all document types", description = "Retrieve all document types in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document types retrieved successfully")
    })
    public ResponseEntity<List<DocumentTypeResponse>> getAllDocumentTypes(
            @Parameter(description = "Filter only active document types")
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        
        List<DocumentTypeEntity> documentTypes = activeOnly ? 
            listDocumentTypesUseCase.executeActiveOnly() : 
            listDocumentTypesUseCase.execute();
        
        List<DocumentTypeResponse> response = documentTypes.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/paginated")
    @Operation(summary = "List document types with pagination", description = "Retrieve document types with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paginated document types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<PaginatedResponse<DocumentTypeResponse>> getDocumentTypesWithPagination(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (1-100)")
            @RequestParam(defaultValue = "10") int size) {
        
        var result = listWithPaginationUseCase.execute(page, size);
        
        List<DocumentTypeResponse> content = result.items().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        PaginatedResponse<DocumentTypeResponse> response = PaginatedResponse.of(
            content, 
            result.currentPage(), 
            result.pageSize(), 
            result.totalPages(), 
            result.totalCount()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search document types", description = "Search document types by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<List<DocumentTypeResponse>> searchDocumentTypes(
            @Parameter(description = "Name to search for")
            @RequestParam String name) {
        
        List<DocumentTypeEntity> documentTypes = searchDocumentTypesUseCase.execute(name);
        
        List<DocumentTypeResponse> response = documentTypes.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{code}")
    @Operation(summary = "Get document type by code", description = "Retrieve a specific document type by its code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document type found",
            content = @Content(schema = @Schema(implementation = DocumentTypeResponse.class))),
        @ApiResponse(responseCode = "404", description = "Document type not found")
    })
    public ResponseEntity<DocumentTypeResponse> getDocumentTypeByCode(
            @Parameter(description = "Document type code")
            @PathVariable String code) {
        
        return getDocumentTypeUseCase.execute(code)
            .map(this::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create a new document type", description = "Register a new document type in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Document type created successfully",
            content = @Content(schema = @Schema(implementation = DocumentTypeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate code"),
        @ApiResponse(responseCode = "409", description = "Document type code already exists")
    })
    public ResponseEntity<DocumentTypeResponse> createDocumentType(
            @Valid @RequestBody CreateDocumentTypeRequest request) {
        
        CreateDocumentTypeCommand command = CreateDocumentTypeCommand.create(
            request.code(),
            request.name(),
            request.description(),
            request.validationRegex(),
            request.active()
        );
        
        DocumentTypeEntity created = createDocumentTypeUseCase.execute(command);
        DocumentTypeResponse response = toResponse(created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    private DocumentTypeResponse toResponse(DocumentTypeEntity entity) {
        return new DocumentTypeResponse(
            entity.getId().getValue(),
            entity.getCode(),
            entity.getName(),
            entity.getDescription(),
            entity.getValidationRegex(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}