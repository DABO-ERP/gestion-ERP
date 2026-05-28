package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.AmenityDefinition;
import com.daboerp.gestion.domain.valueobject.AmenityDefinitionId;
import com.daboerp.gestion.infrastructure.persistence.entity.AmenityDefinitionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AmenityDefinitionMapper {

    public AmenityDefinitionJpaEntity toJpaEntity(AmenityDefinition definition) {
        AmenityDefinitionJpaEntity entity = new AmenityDefinitionJpaEntity();
        entity.setId(definition.getId().getValue());
        entity.setName(definition.getName());
        return entity;
    }

    public AmenityDefinition toDomainEntity(AmenityDefinitionJpaEntity entity) {
        if (entity == null) return null;
        return AmenityDefinition.reconstitute(
            AmenityDefinitionId.of(entity.getId()),
            entity.getName()
        );
    }
}
