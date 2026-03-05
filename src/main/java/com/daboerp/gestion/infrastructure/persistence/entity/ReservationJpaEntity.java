package com.daboerp.gestion.infrastructure.persistence.entity;

import com.daboerp.gestion.domain.entity.StatusType;
import com.daboerp.gestion.domain.valueobject.Source;
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
 * JPA entity for Reservation persistence.
 */
@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationJpaEntity {
    
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    
    @Column(name = "reservation_code", nullable = false, unique = true, length = 50)
    private String reservationCode;
    
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;
    
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;
    
    @Column(name = "status_id", length = 36)
    private String statusId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_type", nullable = false, length = 50)
    private StatusType statusType;
    
    @Column(name = "status_note", columnDefinition = "TEXT")
    private String statusNote;
    
    @Column(name = "quoted_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal quotedAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 50)
    private Source source;
    
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
    
    @Column(name = "guest_principal_id", nullable = false, length = 36)
    private String guestPrincipalId;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_guests", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "guest_id")
    private List<String> guestIds = new ArrayList<>();
    
    @Column(name = "room_id", nullable = false, length = 36)
    private String roomId;
    
    @Column(name = "stay_id", length = 36)
    private String stayId;
    
    @Column(name = "stay_check_in")
    private LocalDate stayCheckIn;
    
    @Column(name = "stay_check_out")
    private LocalDate stayCheckOut;
}
