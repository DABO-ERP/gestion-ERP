package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.RoomBlock;
import com.daboerp.gestion.domain.repository.RoomBlockRepository;
import com.daboerp.gestion.domain.valueobject.RoomBlockId;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.infrastructure.persistence.jpa.RoomBlockJpaRepository;
import com.daboerp.gestion.infrastructure.persistence.mapper.RoomBlockMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RoomBlockRepositoryImpl implements RoomBlockRepository {

    private final RoomBlockJpaRepository jpaRepository;
    private final RoomBlockMapper mapper;

    public RoomBlockRepositoryImpl(RoomBlockJpaRepository jpaRepository, RoomBlockMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public RoomBlock save(RoomBlock block) {
        var entity = mapper.toJpaEntity(block);
        var saved = jpaRepository.save(entity);
        return mapper.toDomainEntity(saved);
    }

    @Override
    public Optional<RoomBlock> findById(RoomBlockId id) {
        return jpaRepository.findById(id.getValue()).map(mapper::toDomainEntity);
    }

    @Override
    public List<RoomBlock> findByRoomId(RoomId roomId) {
        return jpaRepository.findByRoomIdOrderByStartDateAsc(roomId.getValue()).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoomBlock> findActiveByRoomId(RoomId roomId) {
        return jpaRepository.findByRoomIdAndActiveTrueOrderByStartDateAsc(roomId.getValue()).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoomBlock> findActiveByRoomIdInDateRange(RoomId roomId, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findActiveByRoomIdInDateRange(roomId.getValue(), startDate, endDate).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoomBlock> findActiveBlocksIntersectingDate(LocalDate date) {
        return jpaRepository.findActiveBlocksOnDate(date).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoomBlock> findAllActive() {
        return jpaRepository.findByActiveTrueOrderByStartDateAsc().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(RoomBlockId id) {
        jpaRepository.deleteById(id.getValue());
    }
}
