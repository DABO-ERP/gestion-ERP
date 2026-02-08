package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.RoomStatus;
import com.daboerp.gestion.infrastructure.persistence.jpa.RoomJpaRepository;
import com.daboerp.gestion.infrastructure.persistence.mapper.RoomMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of RoomRepository using Spring Data JPA.
 */
@Repository
public class RoomRepositoryImpl implements RoomRepository {
    
    private final RoomJpaRepository jpaRepository;
    private final RoomMapper mapper;
    
    public RoomRepositoryImpl(RoomJpaRepository jpaRepository, RoomMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }
    
    @Override
    public Room save(Room room) {
        var entity = mapper.toJpaEntity(room);
        var saved = jpaRepository.save(entity);
        return mapper.toDomainEntity(saved);
    }
    
    @Override
    public Optional<Room> findById(RoomId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public Optional<Room> findByRoomNumber(Integer roomNumber) {
        return jpaRepository.findByRoomNumber(roomNumber)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public List<Room> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Room> findByStatus(RoomStatus status) {
        return jpaRepository.findByRoomStatus(status).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        return jpaRepository.findAvailableRooms(checkIn, checkOut).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Room> findAvailableByCapacity(int minCapacity, LocalDate checkIn, LocalDate checkOut) {
        return jpaRepository.findAvailableByCapacity(minCapacity, checkIn, checkOut).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(RoomId id) {
        jpaRepository.deleteById(id.getValue());
    }
    
    @Override
    public boolean existsByRoomNumber(Integer roomNumber) {
        return jpaRepository.existsByRoomNumber(roomNumber);
    }
}
