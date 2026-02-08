package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.valueobject.RoomTypeId;

import java.util.List;
import java.util.Optional;

/**
 * RoomType repository interface - domain layer contract.
 */
public interface RoomTypeRepository {
    
    /**
     * Save a new room type or update an existing one.
     */
    RoomType save(RoomType roomType);
    
    /**
     * Find a room type by its unique identifier.
     */
    Optional<RoomType> findById(RoomTypeId id);
    
    /**
     * Find a room type by name.
     */
    Optional<RoomType> findByName(String name);
    
    /**
     * Find all room types.
     */
    List<RoomType> findAll();
    
    /**
     * Delete a room type.
     */
    void delete(RoomTypeId id);
    
    /**
     * Check if a room type exists by name.
     */
    boolean existsByName(String name);
}
