package com.daboerp.gestion.application.usecase.reservation;

import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.ReservationId;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Use case for checking out a reservation.
 */
public class CheckOutReservationUseCase {
    
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    
    public CheckOutReservationUseCase(ReservationRepository reservationRepository,
                                     RoomRepository roomRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }
    
    public Reservation execute(CheckOutCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        
        ReservationId id = ReservationId.of(command.reservationId());
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", command.reservationId()));
        
        LocalDate checkOutDate = command.actualCheckOutDate() != null ? 
            command.actualCheckOutDate() : LocalDate.now();

        Room roomToUpdate = reservation.getRoom();
        reservation.checkOut(checkOutDate);
        if (roomToUpdate != null) {
            roomRepository.save(roomToUpdate);
        }

        return reservationRepository.save(reservation);
    }
    
    public record CheckOutCommand(
        String reservationId,
        LocalDate actualCheckOutDate
    ) {
        public CheckOutCommand {
            Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        }
    }
}
