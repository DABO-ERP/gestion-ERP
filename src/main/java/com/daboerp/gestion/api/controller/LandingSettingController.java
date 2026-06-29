package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.LandingSettingResponse;
import com.daboerp.gestion.api.dto.SaveLandingSettingRequest;
import com.daboerp.gestion.application.usecase.landing.GetAllLandingSettingsUseCase;
import com.daboerp.gestion.application.usecase.landing.GetLandingSettingUseCase;
import com.daboerp.gestion.application.usecase.landing.SaveLandingSettingUseCase;
import com.daboerp.gestion.domain.entity.LandingSetting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Landing Settings", description = "APIs for managing landing page configuration")
public class LandingSettingController {

    private final GetAllLandingSettingsUseCase getAllLandingSettingsUseCase;
    private final GetLandingSettingUseCase getLandingSettingUseCase;
    private final SaveLandingSettingUseCase saveLandingSettingUseCase;

    public LandingSettingController(GetAllLandingSettingsUseCase getAllLandingSettingsUseCase,
                                    GetLandingSettingUseCase getLandingSettingUseCase,
                                    SaveLandingSettingUseCase saveLandingSettingUseCase) {
        this.getAllLandingSettingsUseCase = getAllLandingSettingsUseCase;
        this.getLandingSettingUseCase = getLandingSettingUseCase;
        this.saveLandingSettingUseCase = saveLandingSettingUseCase;
    }

    @GetMapping("/api/v1/landing-settings")
    @Operation(summary = "Get all landing settings")
    public ResponseEntity<List<LandingSettingResponse>> getAll() {
        List<LandingSettingResponse> response = getAllLandingSettingsUseCase.execute().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/landing-settings/{key}")
    @Operation(summary = "Get a landing setting by key")
    public ResponseEntity<LandingSettingResponse> getByKey(
            @Parameter(description = "Setting key") @PathVariable String key) {
        return getLandingSettingUseCase.execute(key)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/v1/landing-settings/{key}")
    @Operation(summary = "Save or update a landing setting")
    public ResponseEntity<Void> save(
            @Parameter(description = "Setting key") @PathVariable String key,
            @RequestBody SaveLandingSettingRequest request) {
        saveLandingSettingUseCase.execute(key, request.value());
        return ResponseEntity.ok().build();
    }

    private LandingSettingResponse toResponse(LandingSetting setting) {
        return new LandingSettingResponse(setting.getKey(), setting.getValue());
    }
}
