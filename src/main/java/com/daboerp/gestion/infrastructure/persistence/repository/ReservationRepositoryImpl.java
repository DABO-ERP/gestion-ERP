package com.daboerp.gestion.infrastructure.persistence.repository;

import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.entity.StatusType;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.domain.valueobject.ReservationId;
import com.daboerp.gestion.domain.valueobject.RoomId;
import com.daboerp.gestion.infrastructure.persistence.jpa.ReservationJpaRepository;
import com.daboerp.gestion.infrastructure.persistence.mapper.ReservationMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ReservationRepository using Spring Data JPA.
 */
@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    
    private final ReservationJpaRepository jpaRepository;
    private final ReservationMapper mapper;
    
    public ReservationRepositoryImpl(ReservationJpaRepository jpaRepository, ReservationMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }
    
    @Override
    public Reservation save(Reservation reservation) {
        var entity = mapper.toJpaEntity(reservation);
        var saved = jpaRepository.save(entity);
        return mapper.toDomainEntity(saved);
    }
    
    @Override
    public Optional<Reservation> findById(ReservationId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public Optional<Reservation> findByReservationCode(String reservationCode) {
        return jpaRepository.findByReservationCode(reservationCode)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public List<Reservation> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByGuest(GuestId guestId) {
        return jpaRepository.findByGuestPrincipalId(guestId.getValue()).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByRoom(RoomId roomId) {
        return jpaRepository.findByRoomId(roomId.getValue()).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByStatus(StatusType statusType) {
        return jpaRepository.findByStatusType(statusType).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByDateRange(startDate, endDate).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findActiveReservations() {
        return jpaRepository.findActiveReservations().stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findCheckInsForDate(LocalDate date) {
        return jpaRepository.findByCheckIn(date).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findCheckOutsForDate(LocalDate date) {
        return jpaRepository.findByCheckOut(date).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findOverlappingReservations(RoomId roomId, LocalDate checkIn, LocalDate checkOut) {
        return jpaRepository.findOverlappingReservations(roomId.getValue(), checkIn, checkOut).stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(ReservationId id) {
        jpaRepository.deleteById(id.getValue());
    }
}
