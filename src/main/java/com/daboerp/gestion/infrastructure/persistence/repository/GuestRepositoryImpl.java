package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.infrastructure.persistence.entity.GuestJpaEntity;
import com.daboerp.gestion.infrastructure.persistence.jpa.GuestJpaRepository;
import com.daboerp.gestion.infrastructure.persistence.mapper.GuestMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of GuestRepository using Spring Data JPA.
 * Bridges domain and infrastructure layers.
 */
@Repository
public class GuestRepositoryImpl implements GuestRepository {
    
    private final GuestJpaRepository jpaRepository;
    private final GuestMapper mapper;
    
    public GuestRepositoryImpl(GuestJpaRepository jpaRepository, GuestMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }
    
    @Override
    public Guest save(Guest guest) {
        GuestJpaEntity entity = mapper.toJpaEntity(guest);
        GuestJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomainEntity(saved);
    }
    
    @Override
    public Optional<Guest> findById(GuestId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public Optional<Guest> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public Optional<Guest> findByDocumentNumber(String documentNumber) {
        return jpaRepository.findByDocumentNumber(documentNumber)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public List<Guest> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Guest> searchByName(String name) {
        return jpaRepository.searchByName(name).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(GuestId id) {
        jpaRepository.deleteById(id.getValue());
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
