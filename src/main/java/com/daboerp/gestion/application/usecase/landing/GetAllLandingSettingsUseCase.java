package com.daboerp.gestion.application.usecase.landing;

import com.daboerp.gestion.domain.entity.LandingSetting;
import com.daboerp.gestion.domain.repository.LandingSettingRepository;

import java.util.List;
import java.util.Objects;

public class GetAllLandingSettingsUseCase {

    private final LandingSettingRepository landingSettingRepository;

    public GetAllLandingSettingsUseCase(LandingSettingRepository landingSettingRepository) {
        this.landingSettingRepository = Objects.requireNonNull(landingSettingRepository, "LandingSettingRepository cannot be null");
    }

    public List<LandingSetting> execute() {
        return landingSettingRepository.findAll();
    }
}
