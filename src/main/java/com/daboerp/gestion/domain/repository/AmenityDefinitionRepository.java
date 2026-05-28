package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.AmenityDefinition;
import com.daboerp.gestion.domain.valueobject.AmenityDefinitionId;

import java.util.List;
import java.util.Optional;

public interface AmenityDefinitionRepository {

    AmenityDefinition save(AmenityDefinition definition);

    Optional<AmenityDefinition> findById(AmenityDefinitionId id);

    Optional<AmenityDefinition> findByName(String name);

    List<AmenityDefinition> findAll();

    void delete(AmenityDefinitionId id);
}
