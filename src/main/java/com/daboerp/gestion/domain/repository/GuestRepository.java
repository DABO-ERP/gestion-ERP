package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.valueobject.GuestId;

import java.util.List;
import java.util.Optional;

/**
 * Guest repository interface - domain layer contract.
 * No implementation details, pure abstraction.
 */
public interface GuestRepository {
    
    /**
     * Save a new guest or update an existing one.
     */
    Guest save(Guest guest);
    
    /**
     * Find a guest by their unique identifier.
     */
    Optional<Guest> findById(GuestId id);
    
    /**
     * Find a guest by email.
     */
    Optional<Guest> findByEmail(String email);
    
    /**
     * Find a guest by document number.
     */
    Optional<Guest> findByDocumentNumber(String documentNumber);
    
    /**
     * Find all guests.
     */
    List<Guest> findAll();
    
    /**
     * Search guests by name (first name or last name).
     */
    List<Guest> searchByName(String name);
    
    /**
     * Delete a guest.
     */
    void delete(GuestId id);
    
    /**
     * Check if a guest exists by email.
     */
    boolean existsByEmail(String email);
}
