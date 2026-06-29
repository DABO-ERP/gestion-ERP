package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.LandingSetting;
import com.daboerp.gestion.domain.repository.LandingSettingRepository;
import com.daboerp.gestion.infrastructure.persistence.entity.LandingSettingJpaEntity;
import com.daboerp.gestion.infrastructure.persistence.jpa.LandingSettingJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of LandingSettingRepository using Spring Data JPA.
 */
@Repository
public class LandingSettingRepositoryImpl implements LandingSettingRepository {

    private final LandingSettingJpaRepository jpaRepository;

    public LandingSettingRepositoryImpl(LandingSettingJpaRepository jpaRepository) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
    }

    @Override
    public Optional<LandingSetting> findByKey(String key) {
        return jpaRepository.findById(key)
                .map(this::toDomain);
    }

    @Override
    public List<LandingSetting> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void save(LandingSetting setting) {
        jpaRepository.save(toJpa(setting));
    }

    private LandingSetting toDomain(LandingSettingJpaEntity entity) {
        return new LandingSetting(entity.getKey(), entity.getValue());
    }

    private LandingSettingJpaEntity toJpa(LandingSetting setting) {
        return new LandingSettingJpaEntity(setting.getKey(), setting.getValue());
    }
}
