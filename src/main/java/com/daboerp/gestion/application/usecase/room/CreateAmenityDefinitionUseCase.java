package com.daboerp.gestion.application.usecase.room;

import com.daboerp.gestion.application.exception.ResourceAlreadyExistsException;
import com.daboerp.gestion.domain.entity.AmenityDefinition;
import com.daboerp.gestion.domain.repository.AmenityDefinitionRepository;

import java.util.Objects;

public class CreateAmenityDefinitionUseCase {

    private final AmenityDefinitionRepository repository;

    public CreateAmenityDefinitionUseCase(AmenityDefinitionRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public AmenityDefinition execute(CreateAmenityDefinitionCommand command) {
        Objects.requireNonNull(command);

        if (repository.findByName(command.name).isPresent()) {
            throw new ResourceAlreadyExistsException("AmenityDefinition", command.name);
        }

        AmenityDefinition definition = AmenityDefinition.create(command.name);
        return repository.save(definition);
    }

    public record CreateAmenityDefinitionCommand(String name) {
        public CreateAmenityDefinitionCommand {
            Objects.requireNonNull(name, "Name cannot be null");
        }
    }
}
