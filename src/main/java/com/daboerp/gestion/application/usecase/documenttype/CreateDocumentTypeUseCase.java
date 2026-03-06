package com.daboerp.gestion.application.usecase.documenttype;

import com.daboerp.gestion.application.command.documenttype.CreateDocumentTypeCommand;
import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.repository.DocumentTypeRepository;

import java.util.Objects;

/**
 * Use case for creating a new document type.
 */
public class CreateDocumentTypeUseCase {
    
    private final DocumentTypeRepository documentTypeRepository;
    
    public CreateDocumentTypeUseCase(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = Objects.requireNonNull(documentTypeRepository, "Document type repository cannot be null");
    }
    
    public DocumentTypeEntity execute(CreateDocumentTypeCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        
        // Check if code already exists
        if (documentTypeRepository.existsByCode(command.code())) {
            throw new IllegalArgumentException("Document type with code '" + command.code() + "' already exists");
        }
        
        // Create new document type entity
        DocumentTypeEntity documentType = DocumentTypeEntity.create(
            command.code(),
            command.name(),
            command.description(),
            command.validationRegex(),
            command.active()
        );
        
        // Save and return
        return documentTypeRepository.save(documentType);
    }
}