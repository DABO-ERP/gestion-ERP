package com.daboerp.gestion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity for LandingSetting persistence.
 */
@Entity
@Table(name = "landing_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LandingSettingJpaEntity {

    @Id
    @Column(name = "key", nullable = false, length = 100)
    private String key;

    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    private String value;
}
