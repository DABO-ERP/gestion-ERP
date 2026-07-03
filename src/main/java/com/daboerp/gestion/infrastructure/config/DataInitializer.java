package com.daboerp.gestion.infrastructure.config;

import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * Initializes seed data for development and non-test environments.
 * Ensures baseline room types exist on application startup.
 */
@Component
@Profile("!test")
public class DataInitializer {

    private final RoomTypeRepository roomTypeRepository;

    public DataInitializer(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @PostConstruct
    public void init() {
        if (!roomTypeRepository.findAll().isEmpty()) {
            return;
        }

        roomTypeRepository.save(RoomType.create(
            "Standard Single", "Single room with basic amenities", 1,
            new BigDecimal("50.00")));

        roomTypeRepository.save(RoomType.create(
            "Standard Double", "Double room for two guests", 2,
            new BigDecimal("80.00")));

        roomTypeRepository.save(RoomType.create(
            "Deluxe Suite", "Luxury suite with premium amenities", 3,
            new BigDecimal("150.00")));

        roomTypeRepository.save(RoomType.create(
            "Dormitory 4-Bed", "Shared dormitory with 4 beds", 4,
            new BigDecimal("25.00")));

        roomTypeRepository.save(RoomType.create(
            "Family Room", "Spacious room for families", 4,
            new BigDecimal("120.00")));
    }
}
