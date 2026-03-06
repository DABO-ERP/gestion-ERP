package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.valueobject.DocumentTypeId;
import com.daboerp.gestion.infrastructure.persistence.entity.DocumentTypeJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between DocumentType domain entity and JPA entity.
 * Handles conversion between domain and infrastructure layers.
 */
@Component
public class DocumentTypeMapper {
    
    public DocumentTypeJpaEntity toJpaEntity(DocumentTypeEntity documentType) {
        DocumentTypeJpaEntity entity = new DocumentTypeJpaEntity();
        entity.setId(documentType.getId().getValue());
        entity.setCode(documentType.getCode());
        entity.setName(documentType.getName());
        entity.setDescription(documentType.getDescription());
        entity.setValidationRegex(documentType.getValidationRegex());
        entity.setActive(documentType.isActive());
        entity.setCreatedAt(documentType.getCreatedAt());
        entity.setUpdatedAt(documentType.getUpdatedAt());
        
        return entity;
    }
    
    public DocumentTypeEntity toDomainEntity(DocumentTypeJpaEntity entity) {
        return DocumentTypeEntity.reconstitute(
            DocumentTypeId.of(entity.getId()),
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