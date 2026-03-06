package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.domain.entity.StatusType;
import com.daboerp.gestion.domain.valueobject.Source;
import com.daboerp.gestion.infrastructure.persistence.entity.ReservationJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.checkIn = :date")
    List<ReservationJpaEntity> findByCheckIn(@Param("date") LocalDate date);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.checkOut = :date")
    List<ReservationJpaEntity> findByCheckOut(@Param("date") LocalDate date);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "r.roomId = :roomId AND " +
           "r.statusType IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "NOT (r.checkOut <= :checkIn OR r.checkIn >= :checkOut)")
    List<ReservationJpaEntity> findOverlappingReservations(@Param("roomId") String roomId,
                                                           @Param("checkIn") LocalDate checkIn,
                                                           @Param("checkOut") LocalDate checkOut);
    
    /**
     * Find reservations by multiple optional filters.
     * Dynamic query based on which parameters are not null.
     */
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "(:status IS NULL OR r.statusType = :status) AND " +
           "(:source IS NULL OR r.source = :source) AND " +
           "(:checkInStart IS NULL OR r.checkIn >= :checkInStart) AND " +
           "(:checkInEnd IS NULL OR r.checkIn <= :checkInEnd) AND " +
           "(:stayStart IS NULL OR r.checkOut >= :stayStart) AND " +
           "(:stayEnd IS NULL OR r.checkIn <= :stayEnd)")
    List<ReservationJpaEntity> findByFilters(@Param("status") StatusType status,
                                            @Param("source") Source source,
                                            @Param("checkInStart") LocalDate checkInStart,
                                            @Param("checkInEnd") LocalDate checkInEnd,
                                            @Param("stayStart") LocalDate stayStart,
                                            @Param("stayEnd") LocalDate stayEnd);
    
    /**
     * Find reservations by multiple optional filters with pagination.
     */
    @Query("SELECT r FROM ReservationJpaEntity r WHERE " +
           "(:status IS NULL OR r.statusType = :status) AND " +
           "(:source IS NULL OR r.source = :source) AND " +
           "(:checkInStart IS NULL OR r.checkIn >= :checkInStart) AND " +
           "(:checkInEnd IS NULL OR r.checkIn <= :checkInEnd) AND " +
           "(:stayStart IS NULL OR r.checkOut >= :stayStart) AND " +
           "(:stayEnd IS NULL OR r.checkIn <= :stayEnd)")
    Page<ReservationJpaEntity> findByFiltersWithPagination(@Param("status") StatusType status,
                                                          @Param("source") Source source,
                                                          @Param("checkInStart") LocalDate checkInStart,
                                                          @Param("checkInEnd") LocalDate checkInEnd,
                                                          @Param("stayStart") LocalDate stayStart,
                                                          @Param("stayEnd") LocalDate stayEnd,
                                                          Pageable pageable);
    
    /**
     * Count reservations by multiple optional filters.
     */
    @Query("SELECT COUNT(r) FROM ReservationJpaEntity r WHERE " +
           "(:status IS NULL OR r.statusType = :status) AND " +
           "(:source IS NULL OR r.source = :source) AND " +
           "(:checkInStart IS NULL OR r.checkIn >= :checkInStart) AND " +
           "(:checkInEnd IS NULL OR r.checkIn <= :checkInEnd) AND " +
           "(:stayStart IS NULL OR r.checkOut >= :stayStart) AND " +
           "(:stayEnd IS NULL OR r.checkIn <= :stayEnd)")
    long countByFilters(@Param("status") StatusType status,
                       @Param("source") Source source,
                       @Param("checkInStart") LocalDate checkInStart,
                       @Param("checkInEnd") LocalDate checkInEnd,
                       @Param("stayStart") LocalDate stayStart,
                       @Param("stayEnd") LocalDate stayEnd);
}
