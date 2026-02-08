package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.*;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.ReservationId;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.infrastructure.persistence.entity.ReservationJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper between Reservation domain entity and JPA entity.
 */
@Component
public class ReservationMapper {
    
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    
    public ReservationMapper(GuestRepository guestRepository, RoomRepository roomRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }
    
    public ReservationJpaEntity toJpaEntity(Reservation reservation) {
        ReservationJpaEntity entity = new ReservationJpaEntity();
        entity.setId(reservation.getId().getValue());
        entity.setReservationCode(reservation.getReservationCode());
        entity.setCheckIn(reservation.getCheckIn());
        entity.setCheckOut(reservation.getCheckOut());
        entity.setQuotedAmount(reservation.getQuotedAmount());
        entity.setSource(reservation.getSource());
        entity.setCreatedAt(reservation.getCreatedAt());
        
        // Map status
        ReservationStatus status = reservation.getStatus();
        entity.setStatusId(status.getId());
        entity.setStatusType(status.getStatusType());
        entity.setStatusNote(status.getNote());
        
        // Map guest principal
        entity.setGuestPrincipalId(reservation.getGuestPrincipal().getId().getValue());
        
        // Map all guests
        List<String> guestIds = reservation.getGuests().stream()
            .map(guest -> guest.getId().getValue())
            .collect(Collectors.toList());
        entity.setGuestIds(guestIds);
        
        // Map room
        entity.setRoomId(reservation.getRoom().getId().getValue());
        
        // Map stay if present
        if (reservation.getStay() != null) {
            Stay stay = reservation.getStay();
            entity.setStayId(stay.getId());
            entity.setStayCheckIn(stay.getCheckIn());
            entity.setStayCheckOut(stay.getCheckOut());
        }
        
        return entity;
    }
    
    public Reservation toDomainEntity(ReservationJpaEntity entity) {
        // Load guest principal
        Guest guestPrincipal = guestRepository.findById(GuestId.of(entity.getGuestPrincipalId()))
            .orElseThrow(() -> new IllegalStateException("Guest not found: " + entity.getGuestPrincipalId()));
        
        // Load room
        Room room = roomRepository.findById(RoomId.of(entity.getRoomId()))
            .orElseThrow(() -> new IllegalStateException("Room not found: " + entity.getRoomId()));
        
        // Load all guests
        List<Guest> guests = entity.getGuestIds().stream()
            .map(guestId -> guestRepository.findById(GuestId.of(guestId))
                .orElseThrow(() -> new IllegalStateException("Guest not found: " + guestId)))
            .collect(Collectors.toList());
        
        // Reconstitute status
        ReservationStatus status = ReservationStatus.reconstitute(
            entity.getStatusId(),
            entity.getStatusType(),
            entity.getStatusNote()
        );
        
        // Reconstitute stay if present
        Stay stay = null;
        if (entity.getStayId() != null) {
            stay = Stay.reconstitute(
                entity.getStayId(),
                entity.getStayCheckIn(),
                entity.getStayCheckOut()
            );
        }
        
        return Reservation.reconstitute(
            ReservationId.of(entity.getId()),
            entity.getReservationCode(),
            entity.getCheckIn(),
            entity.getCheckOut(),
            status,
            entity.getQuotedAmount(),
            entity.getSource(),
            guestPrincipal,
            guests,
            room,
            stay,
            entity.getCreatedAt()
        );
    }
}
