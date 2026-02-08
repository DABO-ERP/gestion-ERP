package com.daboerp.gestion.infrastructure.persistence.jpa;

import com.daboerp.gestion.domain.valueobject.RoomStatus;
import com.daboerp.gestion.infrastructure.persistence.entity.RoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Room persistence.
 */
@Repository
public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, String> {
    
    Optional<RoomJpaEntity> findByRoomNumber(Integer roomNumber);
    
    List<RoomJpaEntity> findByRoomStatus(RoomStatus status);
    
    boolean existsByRoomNumber(Integer roomNumber);
    
    @Query("SELECT r FROM RoomJpaEntity r WHERE r.roomStatus = 'AVAILABLE' AND " +
           "r.id NOT IN (" +
           "  SELECT res.roomId FROM ReservationJpaEntity res WHERE " +
           "  res.statusType IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "  NOT (res.checkOut <= :checkIn OR res.checkIn >= :checkOut)" +
           ")")
    List<RoomJpaEntity> findAvailableRooms(@Param("checkIn") LocalDate checkIn, 
                                           @Param("checkOut") LocalDate checkOut);
    
    @Query("SELECT r FROM RoomJpaEntity r WHERE r.roomStatus = 'AVAILABLE' AND " +
           "r.roomTypeMaxOccupancy >= :minCapacity AND " +
           "r.id NOT IN (" +
           "  SELECT res.roomId FROM ReservationJpaEntity res WHERE " +
           "  res.statusType IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "  NOT (res.checkOut <= :checkIn OR res.checkIn >= :checkOut)" +
           ")")
    List<RoomJpaEntity> findAvailableByCapacity(@Param("minCapacity") int minCapacity,
                                                @Param("checkIn") LocalDate checkIn,
                                                @Param("checkOut") LocalDate checkOut);
}
