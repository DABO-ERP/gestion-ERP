package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.GuestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Guest persistence.
 */
@Repository
public interface GuestJpaRepository extends JpaRepository<GuestJpaEntity, String> {
    
    Optional<GuestJpaEntity> findByEmail(String email);
    
    Optional<GuestJpaEntity> findByDocumentNumber(String documentNumber);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT g FROM GuestJpaEntity g WHERE " +
           "LOWER(g.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(g.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<GuestJpaEntity> searchByName(@Param("name") String name);
}
