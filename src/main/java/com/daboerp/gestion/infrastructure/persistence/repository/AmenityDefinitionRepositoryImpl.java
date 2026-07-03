package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.AmenityDefinition;
import com.daboerp.gestion.domain.repository.AmenityDefinitionRepository;
import com.daboerp.gestion.domain.valueobject.AmenityDefinitionId;
import com.daboerp.gestion.infrastructure.persistence.jpa.AmenityDefinitionJpaRepository;
import com.daboerp.gestion.infrastructure.persistence.mapper.AmenityDefinitionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of AmenityDefinitionRepository using Spring Data JPA.
 * Bridges domain and infrastructure layers.
 */
@Repository
public class AmenityDefinitionRepositoryImpl implements AmenityDefinitionRepository {

    private final AmenityDefinitionJpaRepository jpaRepository;
    private final AmenityDefinitionMapper mapper;

    public AmenityDefinitionRepositoryImpl(AmenityDefinitionJpaRepository jpaRepository, AmenityDefinitionMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public AmenityDefinition save(AmenityDefinition definition) {
        var entity = mapper.toJpaEntity(definition);
        var saved = jpaRepository.save(entity);
        return mapper.toDomainEntity(saved);
    }

    @Override
    public Optional<AmenityDefinition> findById(AmenityDefinitionId id) {
        return jpaRepository.findById(id.getValue()).map(mapper::toDomainEntity);
    }

    @Override
    public Optional<AmenityDefinition> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toDomainEntity);
    }

    @Override
    public List<AmenityDefinition> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(AmenityDefinitionId id) {
        jpaRepository.deleteById(id.getValue());
    }
}
