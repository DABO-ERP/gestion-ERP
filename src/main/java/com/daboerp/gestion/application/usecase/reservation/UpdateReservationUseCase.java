package com.daboerp.gestion.application.usecase.reservation;

import com.daboerp.gestion.application.exception.BusinessRuleViolationException;
import com.daboerp.gestion.application.exception.ResourceNotFoundException;
import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.entity.Room;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.ReservationId;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.Source;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UpdateReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public UpdateReservationUseCase(ReservationRepository reservationRepository,
                                    GuestRepository guestRepository,
                                    RoomRepository roomRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }

    public Reservation execute(UpdateReservationCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        ReservationId id = ReservationId.of(command.reservationId());
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation", command.reservationId()));

        if (command.checkIn() != null && command.checkOut() != null) {
            reservation.updateDates(command.checkIn(), command.checkOut());
        }

        if (command.quotedAmount() != null) {
            reservation.updateQuotedAmount(command.quotedAmount());
        }

        if (command.source() != null) {
            reservation.setSource(command.source());
        }

        if (command.guestPrincipalId() != null) {
            GuestId guestId = GuestId.of(command.guestPrincipalId());
            Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", command.guestPrincipalId()));
            reservation.setGuestPrincipal(guest);
        }

        if (command.roomId() != null) {
            RoomId roomId = RoomId.of(command.roomId());
            Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", command.roomId()));

            if (!room.isAvailable() && !room.getId().equals(reservation.getRoom().getId())) {
                throw new BusinessRuleViolationException("Room " + room.getRoomNumber() + " is not available");
            }

            List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                roomId, reservation.getCheckIn(), reservation.getCheckOut()
            );
            boolean selfOverlap = overlapping.stream()
                .anyMatch(r -> !r.getId().equals(reservation.getId()));
            if (!overlapping.isEmpty() && selfOverlap) {
                throw new BusinessRuleViolationException(
                    "Room " + room.getRoomNumber() + " is already reserved for the selected dates"
                );
            }

            reservation.updateRoom(room);
        }

        if (command.additionalGuestIds() != null) {
            String principalId = reservation.getGuestPrincipal().getId().getValue();
            List<String> currentIds = reservation.getGuests().stream()
                .map(g -> g.getId().getValue())
                .collect(Collectors.toList());

            List<Guest> guestsToRemove = reservation.getGuests().stream()
                .filter(g -> !g.getId().getValue().equals(principalId))
                .filter(g -> !command.additionalGuestIds().contains(g.getId().getValue()))
                .collect(Collectors.toList());
            for (Guest guestToRemove : guestsToRemove) {
                reservation.removeGuest(guestToRemove);
            }

            for (String guestId : command.additionalGuestIds()) {
                if (!currentIds.contains(guestId)) {
                    GuestId addGuestId = GuestId.of(guestId);
                    Guest additionalGuest = guestRepository.findById(addGuestId)
                        .orElseThrow(() -> new ResourceNotFoundException("Guest", guestId));
                    reservation.addGuest(additionalGuest);
                }
            }
        }

        return reservationRepository.save(reservation);
    }

    public record UpdateReservationCommand(
        String reservationId,
        LocalDate checkIn,
        LocalDate checkOut,
        BigDecimal quotedAmount,
        Source source,
        String guestPrincipalId,
        String roomId,
        List<String> additionalGuestIds
    ) {
        public UpdateReservationCommand {
            Objects.requireNonNull(reservationId, "Reservation ID cannot be null");
        }
    }
}
