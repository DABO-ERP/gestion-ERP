package com.daboerp.gestion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity for Bed persistence.
 */
@Entity
@Table(name = "beds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BedJpaEntity {
    
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    
    @Column(name = "bed_number", nullable = false)
    private Integer bedNumber;
    
    @Column(name = "room_id", nullable = false, length = 36)
    private String roomId;
}
