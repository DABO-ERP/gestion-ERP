package com.daboerp.gestion.domain.factory;

/**
 * Base interface for domain entity factories.
 * Provides consistent creation patterns across all entities.
 */
public interface EntityFactory<T, B> {
    
    /**
     * Create a new entity using the provided builder.
     */
    T create(B builder);
    
    /**
     * Reconstitute an entity from persistence data.
     */
    T reconstitute(B builder);
    
    /**
     * Validate the builder before entity creation.
     */
    default void validate(B builder) {
        // Default implementation - can be overridden
    }
}