package com.daboerp.gestion.application.usecase.documenttype;

import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.repository.DocumentTypeRepository;

import java.util.List;
import java.util.Objects;

/**
 * Use case for listing document types with pagination.
 */
public class ListDocumentTypesWithPaginationUseCase {
    
    private final DocumentTypeRepository documentTypeRepository;
    
    public ListDocumentTypesWithPaginationUseCase(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = Objects.requireNonNull(documentTypeRepository, "Document type repository cannot be null");
    }
    
    public PaginationResult execute(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
        
        List<DocumentTypeEntity> items = documentTypeRepository.findWithPagination(page, size);
        long totalCount = documentTypeRepository.count();
        long totalPages = (totalCount + size - 1) / size; // Ceiling division
        
        return new PaginationResult(items, page, size, totalPages, totalCount);
    }
    
    /**
     * Result object for paginated queries.
     */
    public record PaginationResult(
        List<DocumentTypeEntity> items,
        int currentPage,
        int pageSize,
        long totalPages,
        long totalCount
    ) {}
}