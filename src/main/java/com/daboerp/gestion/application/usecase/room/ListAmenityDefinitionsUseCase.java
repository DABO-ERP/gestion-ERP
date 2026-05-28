package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.domain.entity.AmenityDefinition;
import com.daboerp.gestion.domain.repository.AmenityDefinitionRepository;

import java.util.List;
import java.util.Objects;

public class ListAmenityDefinitionsUseCase {

    private final AmenityDefinitionRepository repository;

    public ListAmenityDefinitionsUseCase(AmenityDefinitionRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public List<AmenityDefinition> execute() {
        return repository.findAll();
    }
}
