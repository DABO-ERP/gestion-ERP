package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.DocumentTypeJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for DocumentType persistence.
 */
@Repository
public interface DocumentTypeJpaRepository extends JpaRepository<DocumentTypeJpaEntity, String> {
    
    Optional<DocumentTypeJpaEntity> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<DocumentTypeJpaEntity> findByActiveTrue();
    
    @Query("SELECT dt FROM DocumentTypeJpaEntity dt WHERE " +
           "LOWER(dt.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<DocumentTypeJpaEntity> findByNameContaining(@Param("name") String name);
    
    Page<DocumentTypeJpaEntity> findAll(Pageable pageable);
}