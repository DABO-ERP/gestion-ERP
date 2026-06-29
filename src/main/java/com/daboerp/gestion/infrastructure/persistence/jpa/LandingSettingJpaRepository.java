package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.infrastructure.persistence.entity.LandingSettingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandingSettingJpaRepository extends JpaRepository<LandingSettingJpaEntity, String> {
}
