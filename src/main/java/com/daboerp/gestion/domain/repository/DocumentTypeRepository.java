package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.valueobject.DocumentTypeId;

import java.util.List;
import java.util.Optional;

/**
 * Document Type repository interface - domain layer contract.
 * No implementation details, pure abstraction.
 */
public interface DocumentTypeRepository {
    
    /**
     * Save a new document type or update an existing one.
     */
    DocumentTypeEntity save(DocumentTypeEntity documentType);
    
    /**
     * Find a document type by their unique identifier.
     */
    Optional<DocumentTypeEntity> findById(DocumentTypeId id);
    
    /**
     * Find a document type by code.
     */
    Optional<DocumentTypeEntity> findByCode(String code);
    
    /**
     * Find all document types.
     */
    List<DocumentTypeEntity> findAll();
    
    /**
     * Find only active document types.
     */
    List<DocumentTypeEntity> findAllActive();
    
    /**
     * Find document types containing the given name.
     */
    List<DocumentTypeEntity> findByNameContaining(String name);
    
    /**
     * Find document types with pagination.
     */
    List<DocumentTypeEntity> findWithPagination(int page, int size);
    
    /**
     * Get total count of document types.
     */
    long count();
    
    /**
     * Delete a document type by ID.
     */
    void deleteById(DocumentTypeId id);
    
    /**
     * Check if a document type exists by code.
     */
    boolean existsByCode(String code);
}