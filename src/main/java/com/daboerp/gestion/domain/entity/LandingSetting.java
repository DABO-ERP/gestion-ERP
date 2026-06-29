package com.daboerp.gestion.domain.entity;

import java.util.Objects;

/**
 * LandingSetting entity.
 * Represents a key-value configuration entry for the landing page.
 * No framework dependencies - pure domain model.
 */
public class LandingSetting {

    private final String key;
    private final String value;

    public LandingSetting(String key, String value) {
        this.key = Objects.requireNonNull(key, "Key cannot be null");
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
