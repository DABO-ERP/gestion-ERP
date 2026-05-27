package com.daboerp.gestion.infrastructure.config;

import com.daboerp.gestion.domain.entity.RoomType;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
            new BigDecimal("50.00"),
            "https://picsum.photos/seed/standard-single/400/300"));

        roomTypeRepository.save(RoomType.create(
            "Standard Double", "Double room for two guests", 2,
            new BigDecimal("80.00"),
            "https://picsum.photos/seed/standard-double/400/300"));

        roomTypeRepository.save(RoomType.create(
            "Deluxe Suite", "Luxury suite with premium amenities", 3,
            new BigDecimal("150.00"),
            "https://picsum.photos/seed/deluxe-suite/400/300"));

        roomTypeRepository.save(RoomType.create(
            "Dormitory 4-Bed", "Shared dormitory with 4 beds", 4,
            new BigDecimal("25.00"),
            "https://picsum.photos/seed/dormitory/400/300"));

        roomTypeRepository.save(RoomType.create(
            "Family Room", "Spacious room for families", 4,
            new BigDecimal("120.00"),
            "https://picsum.photos/seed/family-room/400/300"));
    }
}
