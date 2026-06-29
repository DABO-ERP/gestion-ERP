package com.daboerp.gestion.application.usecase.landing;

import com.daboerp.gestion.domain.entity.LandingSetting;
import com.daboerp.gestion.domain.repository.LandingSettingRepository;

import java.util.Objects;

public class SaveLandingSettingUseCase {

    private final LandingSettingRepository landingSettingRepository;

    public SaveLandingSettingUseCase(LandingSettingRepository landingSettingRepository) {
        this.landingSettingRepository = Objects.requireNonNull(landingSettingRepository, "LandingSettingRepository cannot be null");
    }

    public void execute(String key, String value) {
        LandingSetting setting = new LandingSetting(key, value);
        landingSettingRepository.save(setting);
    }
}
