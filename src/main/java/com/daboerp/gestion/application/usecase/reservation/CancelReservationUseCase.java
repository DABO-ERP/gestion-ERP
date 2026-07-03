package com.daboerp.gestion.application.usecase.reservation;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.util.Objects;

/**
 * Use case for cancelling a reservation.
 */
public class CancelReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public CancelReservationUseCase(ReservationRepository reservationRepository,
                                    RoomRepository roomRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }

    public void execute(String reservationId) {
        Objects.requireNonNull(reservationId, "Reservation ID cannot be null");

        ReservationId id = ReservationId.of(reservationId);
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));

        var room = reservation.getRoom();
        reservation.cancel("Cancelled by user");

        if (room != null) {
            roomRepository.save(room);
        }

        reservationRepository.save(reservation);
    }
}
