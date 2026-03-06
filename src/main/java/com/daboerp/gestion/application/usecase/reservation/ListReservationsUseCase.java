package com.daboerp.gestion.application.usecase.reservation;

import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Use case for listing reservations.
 */
public class ListReservationsUseCase {
    
    private final ReservationRepository reservationRepository;
    
    public ListReservationsUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
    }
    
    public List<Reservation> execute() {
        return reservationRepository.findAll();
    }
    
    public List<Reservation> executeActive() {
        return reservationRepository.findActiveReservations();
    }
    
    public List<Reservation> executeCheckInsForDate(LocalDate date) {
        return reservationRepository.findCheckInsForDate(date);
    }
    
    public List<Reservation> executeCheckOutsForDate(LocalDate date) {
        return reservationRepository.findCheckOutsForDate(date);
    }
    
    public List<Reservation> executeByDateRange(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByDateRange(startDate, endDate);
    }
}
