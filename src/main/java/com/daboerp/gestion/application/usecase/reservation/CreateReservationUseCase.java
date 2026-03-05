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
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.domain.valueobject.Source;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Use case for creating a new reservation.
 */
public class CreateReservationUseCase {
    
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    
    public CreateReservationUseCase(ReservationRepository reservationRepository,
                                   GuestRepository guestRepository,
                                   RoomRepository roomRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
        this.guestRepository = Objects.requireNonNull(guestRepository, "Guest repository cannot be null");
        this.roomRepository = Objects.requireNonNull(roomRepository, "Room repository cannot be null");
    }
    
    public Reservation execute(CreateReservationCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        
        // Get guest
        GuestId guestId = GuestId.of(command.guestPrincipalId());
        Guest guest = guestRepository.findById(guestId)
            .orElseThrow(() -> new ResourceNotFoundException("Guest", command.guestPrincipalId()));
        
        // Get room
        RoomId roomId = RoomId.of(command.roomId());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room", command.roomId()));
        
        // Validate room is available
        if (!room.isAvailable()) {
            throw new BusinessRuleViolationException("Room " + room.getRoomNumber() + " is not available");
        }
        
        // Check for overlapping reservations
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
            roomId, command.checkIn(), command.checkOut()
        );
        if (!overlapping.isEmpty()) {
            throw new BusinessRuleViolationException(
                "Room " + room.getRoomNumber() + " is already reserved for the selected dates"
            );
        }
        
        // Validate capacity
        int guestCount = 1 + (command.additionalGuestIds() != null ? command.additionalGuestIds().size() : 0);
        if (!room.canAccommodate(guestCount)) {
            throw new BusinessRuleViolationException(
                "Room capacity exceeded. Maximum: " + room.getRoomType().getMaxOccupancy() + ", Requested: " + guestCount
            );
        }
        
        // Create reservation
        Reservation reservation = Reservation.create(
            command.checkIn(),
            command.checkOut(),
            command.quotedAmount(),
            command.source(),
            guest,
            room
        );
        
        // Add additional guests
        if (command.additionalGuestIds() != null) {
            for (String additionalGuestId : command.additionalGuestIds()) {
                GuestId addGuestId = GuestId.of(additionalGuestId);
                Guest additionalGuest = guestRepository.findById(addGuestId)
                    .orElseThrow(() -> new ResourceNotFoundException("Guest", additionalGuestId));
                reservation.addGuest(additionalGuest);
            }
        }
        
        return reservationRepository.save(reservation);
    }
    
    public record CreateReservationCommand(
        LocalDate checkIn,
        LocalDate checkOut,
        BigDecimal quotedAmount,
        Source source,
        String guestPrincipalId,
        String roomId,
        List<String> additionalGuestIds
    ) {
        public CreateReservationCommand {
            Objects.requireNonNull(checkIn, "Check-in date cannot be null");
            Objects.requireNonNull(checkOut, "Check-out date cannot be null");
            Objects.requireNonNull(quotedAmount, "Quoted amount cannot be null");
            Objects.requireNonNull(source, "Source cannot be null");
            Objects.requireNonNull(guestPrincipalId, "Guest principal ID cannot be null");
            Objects.requireNonNull(roomId, "Room ID cannot be null");
        }
    }
}
