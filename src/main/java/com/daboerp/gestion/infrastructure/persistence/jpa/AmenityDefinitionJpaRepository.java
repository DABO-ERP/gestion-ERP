package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.AmenityDefinitionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmenityDefinitionJpaRepository extends JpaRepository<AmenityDefinitionJpaEntity, String> {
    Optional<AmenityDefinitionJpaEntity> findByName(String name);
    boolean existsByName(String name);
}
