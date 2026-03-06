package com.daboerp.gestion.application.usecase.documenttype;

import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.repository.DocumentTypeRepository;

import java.util.Objects;
import java.util.Optional;

/**
 * Use case for retrieving a document type by code.
 */
public class GetDocumentTypeUseCase {
    
    private final DocumentTypeRepository documentTypeRepository;
    
    public GetDocumentTypeUseCase(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = Objects.requireNonNull(documentTypeRepository, "Document type repository cannot be null");
    }
    
    public Optional<DocumentTypeEntity> execute(String code) {
        Objects.requireNonNull(code, "Code cannot be null");
        if (code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be blank");
        }
        
        return documentTypeRepository.findByCode(code);
    }
}