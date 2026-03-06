package com.daboerp.gestion.infrastructure.persistence.entity;

import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for Room persistence.
 */
@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomJpaEntity {
    
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    
    @Column(name = "room_number", nullable = false, unique = true)
    private Integer roomNumber;
    
    @Column(name = "room_type_id", nullable = false, length = 36)
    private String roomTypeId;
    
    @Column(name = "room_type_name", nullable = false, length = 100)
    private String roomTypeName;
    
    @Column(name = "room_type_description")
    private String roomTypeDescription;
    
    @Column(name = "room_type_max_occupancy", nullable = false)
    private Integer roomTypeMaxOccupancy;
    
    @Column(name = "room_type_base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal roomTypeBasePrice;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_status", nullable = false, length = 50)
    private RoomStatus roomStatus;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    @Enumerated(EnumType.STRING)
    private List<Amenity> amenities = new ArrayList<>();
    
    @OneToMany(mappedBy = "roomId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BedJpaEntity> beds = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
}
