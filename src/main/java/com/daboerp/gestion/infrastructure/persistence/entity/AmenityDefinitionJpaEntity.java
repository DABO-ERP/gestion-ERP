package com.daboerp.gestion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity for AmenityDefinition persistence.
 * Framework-specific, isolated from domain.
 */
@Entity
@Table(name = "amenity_definitions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AmenityDefinitionJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}
