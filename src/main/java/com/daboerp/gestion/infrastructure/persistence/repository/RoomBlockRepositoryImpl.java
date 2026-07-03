package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;
import com.daboerp.gestion.infrastructure.persistence.jpa.RoomBlockJpaRepository;
import com.daboerp.gestion.infrastructure.persistence.mapper.RoomBlockMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of RoomBlockRepository using Spring Data JPA.
 * Bridges domain and infrastructure layers.
 */
@Repository
public class RoomBlockRepositoryImpl implements RoomBlockRepository {

    private final RoomBlockJpaRepository jpaRepository;
    private final RoomBlockMapper mapper;

    public RoomBlockRepositoryImpl(RoomBlockJpaRepository jpaRepository, RoomBlockMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public List<RoomBlock> findByRoomId(String roomId) {
        return jpaRepository.findByRoomIdOrderByStartDateAsc(roomId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoomBlock> findOverlapping(String roomId, LocalDate start, LocalDate end) {
        return jpaRepository.findOverlapping(roomId, start, end).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoomBlock> findAllOverlappingRange(LocalDate start, LocalDate end) {
        return jpaRepository.findAllOverlappingRange(start, end).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public RoomBlock save(RoomBlock block) {
        var entity = mapper.toJpa(block);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<RoomBlock> findById(String id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
}
