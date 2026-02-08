package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import com.daboerp.gestion.domain.valueobject.RoomTypeId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of RoomTypeRepository.
 * Simplified version for MVP - can be replaced with JPA implementation later.
 */
@Repository
public class RoomTypeRepositoryImpl implements RoomTypeRepository {
    
    private final ConcurrentHashMap<String, RoomType> storage = new ConcurrentHashMap<>();
    
    @Override
    public RoomType save(RoomType roomType) {
        storage.put(roomType.getId().getValue(), roomType);
        return roomType;
    }
    
    @Override
    public Optional<RoomType> findById(RoomTypeId id) {
        return Optional.ofNullable(storage.get(id.getValue()));
    }
    
    @Override
    public Optional<RoomType> findByName(String name) {
        return storage.values().stream()
            .filter(rt -> rt.getName().equalsIgnoreCase(name))
            .findFirst();
    }
    
    @Override
    public List<RoomType> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void delete(RoomTypeId id) {
        storage.remove(id.getValue());
    }
    
    @Override
    public boolean existsByName(String name) {
        return storage.values().stream()
            .anyMatch(rt -> rt.getName().equalsIgnoreCase(name));
    }
}
