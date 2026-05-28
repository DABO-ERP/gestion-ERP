package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.AmenityDefinitionId;

import java.util.Objects;

public class AmenityDefinition {

    private final AmenityDefinitionId id;
    private String name;

    private AmenityDefinition(AmenityDefinitionId id, String name) {
        this.id = Objects.requireNonNull(id, "AmenityDefinition ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public static AmenityDefinition create(String name) {
        return new AmenityDefinition(AmenityDefinitionId.generate(), name);
    }

    public static AmenityDefinition reconstitute(AmenityDefinitionId id, String name) {
        return new AmenityDefinition(id, name);
    }

    public AmenityDefinitionId getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmenityDefinition that = (AmenityDefinition) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
