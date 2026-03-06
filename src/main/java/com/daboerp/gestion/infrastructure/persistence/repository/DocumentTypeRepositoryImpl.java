package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.DocumentTypeEntity;
import com.daboerp.gestion.domain.repository.DocumentTypeRepository;
import com.daboerp.gestion.domain.valueobject.DocumentTypeId;
import com.daboerp.gestion.infrastructure.persistence.entity.DocumentTypeJpaEntity;
import com.daboerp.gestion.infrastructure.persistence.jpa.DocumentTypeJpaRepository;
import com.daboerp.gestion.infrastructure.persistence.mapper.DocumentTypeMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of DocumentTypeRepository using Spring Data JPA.
 * Bridges domain and infrastructure layers.
 */
@Repository
public class DocumentTypeRepositoryImpl implements DocumentTypeRepository {
    
    private final DocumentTypeJpaRepository jpaRepository;
    private final DocumentTypeMapper mapper;
    
    public DocumentTypeRepositoryImpl(DocumentTypeJpaRepository jpaRepository, DocumentTypeMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }
    
    @Override
    public DocumentTypeEntity save(DocumentTypeEntity documentType) {
        DocumentTypeJpaEntity entity = mapper.toJpaEntity(documentType);
        DocumentTypeJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomainEntity(saved);
    }
    
    @Override
    public Optional<DocumentTypeEntity> findById(DocumentTypeId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public Optional<DocumentTypeEntity> findByCode(String code) {
        return jpaRepository.findByCode(code)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public List<DocumentTypeEntity> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<DocumentTypeEntity> findAllActive() {
        return jpaRepository.findByActiveTrue().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<DocumentTypeEntity> findByNameContaining(String name) {
        return jpaRepository.findByNameContaining(name).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<DocumentTypeEntity> findWithPagination(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return jpaRepository.findAll(pageable).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public long count() {
        return jpaRepository.count();
    }
    
    @Override
    public void deleteById(DocumentTypeId id) {
        jpaRepository.deleteById(id.getValue());
    }
    
    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}