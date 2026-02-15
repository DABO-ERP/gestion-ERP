package com.daboerp.gestion.application.usecase.documenttype;

import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.repository.DocumentTypeRepository;

import java.util.List;
import java.util.Objects;

/**
 * Use case for searching document types by name.
 */
public class SearchDocumentTypesUseCase {
    
    private final DocumentTypeRepository documentTypeRepository;
    
    public SearchDocumentTypesUseCase(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = Objects.requireNonNull(documentTypeRepository, "Document type repository cannot be null");
    }
    
    public List<DocumentTypeEntity> execute(String name) {
        if (name == null || name.isBlank()) {
            return documentTypeRepository.findAll();
        }
        
        return documentTypeRepository.findByNameContaining(name.trim());
    }
}