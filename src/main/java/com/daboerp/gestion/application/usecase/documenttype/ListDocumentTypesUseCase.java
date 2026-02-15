package com.daboerp.gestion.application.usecase.documenttype;

import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.repository.DocumentTypeRepository;

import java.util.List;
import java.util.Objects;

/**
 * Use case for listing all document types.
 */
public class ListDocumentTypesUseCase {
    
    private final DocumentTypeRepository documentTypeRepository;
    
    public ListDocumentTypesUseCase(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = Objects.requireNonNull(documentTypeRepository, "Document type repository cannot be null");
    }
    
    public List<DocumentTypeEntity> execute() {
        return documentTypeRepository.findAll();
    }
    
    public List<DocumentTypeEntity> executeActiveOnly() {
        return documentTypeRepository.findAllActive();
    }
}