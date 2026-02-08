package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.domain.entity.StatusType;
import com.daboerp.gestion.infrastructure.persistence.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Reservation persistence.
 */
@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, String> {
    
    Optional<ReservationJpaEntity> findByReservationCode(String reservationCode);
    
    List<ReservationJpaEntity> findByGuestPrincipalId(String guestId);
    
    List<ReservationJpaEntity> findByRoomId(String roomId);
    
    List<ReservationJpaEntity> findByStatusType(StatusType statusType);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "r.checkIn <= :endDate AND r.checkOut >= :startDate")
    List<ReservationJpaEntity> findByDateRange(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "r.statusType IN ('CONFIRMED', 'CHECKED_IN')")
    List<ReservationJpaEntity> findActiveReservations();
    
    List<ReservationJpaEntity> findByCheckIn(LocalDate date);
    
    List<ReservationJpaEntity> findByCheckOut(LocalDate date);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "r.roomId = :roomId AND " +
           "r.statusType IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "NOT (r.checkOut <= :checkIn OR r.checkIn >= :checkOut)")
    List<ReservationJpaEntity> findOverlappingReservations(@Param("roomId") String roomId,
                                                           @Param("checkIn") LocalDate checkIn,
                                                           @Param("checkOut") LocalDate checkOut);
}
