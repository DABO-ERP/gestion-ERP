package com.daboerp.gestion.domain.event.reservation;

import com.daboerp.gestion.domain.event.BaseDomainEvent;
import com.daboerp.gestion.domain.valueobject.ReservationId;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.RoomId;
import lombok.Getter;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Event published when a new reservation is created.
 */
@Getter
public class ReservationCreatedEvent extends BaseDomainEvent {
    
    private final ReservationId reservationId;
    private final String reservationCode;
    private final GuestId guestId;
    private final RoomId roomId;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final BigDecimal quotedAmount;
    
    public ReservationCreatedEvent(ReservationId reservationId, String reservationCode, 
                                 GuestId guestId, RoomId roomId, LocalDate checkIn, 
                                 LocalDate checkOut, BigDecimal quotedAmount) {
        super();
        this.reservationId = reservationId;
        this.reservationCode = reservationCode;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.quotedAmount = quotedAmount;
    }
    
    @Override
    public String getEventType() {
        return "reservation.created";
    }
}