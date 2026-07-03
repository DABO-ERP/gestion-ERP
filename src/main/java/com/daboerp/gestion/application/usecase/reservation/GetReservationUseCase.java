package com.daboerp.gestion.application.usecase.reservation;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.util.Objects;

/**
 * Use case for retrieving a reservation by ID.
 */
public class GetReservationUseCase {

    private final ReservationRepository reservationRepository;

    public GetReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
    }

    public Reservation execute(String reservationId) {
        Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        ReservationId id = ReservationId.of(reservationId);
        return reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));
    }
}
