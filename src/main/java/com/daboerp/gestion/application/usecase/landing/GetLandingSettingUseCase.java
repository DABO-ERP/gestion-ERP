package com.daboerp.gestion.application.usecase.landing;

import com.daboerp.gestion.domain.entity.LandingSetting;
import com.daboerp.gestion.domain.repository.LandingSettingRepository;

import java.util.Objects;
import java.util.Optional;

public class GetLandingSettingUseCase {

    private final LandingSettingRepository landingSettingRepository;

    public GetLandingSettingUseCase(LandingSettingRepository landingSettingRepository) {
        this.landingSettingRepository = Objects.requireNonNull(landingSettingRepository, "LandingSettingRepository cannot be null");
    }

    public Optional<LandingSetting> execute(String key) {
        return landingSettingRepository.findByKey(key);
    }
}
