package com.daboerp.gestion.domain.repository;

import com.daboerp.gestion.domain.entity.LandingSetting;

import java.util.List;
import java.util.Optional;

/**
 * LandingSettingRepository interface - domain layer contract.
 */
public interface LandingSettingRepository {

    Optional<LandingSetting> findByKey(String key);

    List<LandingSetting> findAll();

    void save(LandingSetting setting);
}
