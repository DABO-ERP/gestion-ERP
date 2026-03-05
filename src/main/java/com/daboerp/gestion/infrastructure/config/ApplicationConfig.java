package com.daboerp.gestion.infrastructure.config;

import com.daboerp.gestion.application.usecase.guest.*;
import com.daboerp.gestion.application.usecase.reservation.*;
import com.daboerp.gestion.application.usecase.room.*;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration - wires use cases with dependencies.
 * Dependency Injection configuration for Clean Architecture.
 */
@Configuration
public class ApplicationConfig {
    
    // Guest use cases
    
    @Bean
    public CreateGuestUseCase createGuestUseCase(GuestRepository guestRepository) {
        return new CreateGuestUseCase(guestRepository);
    }
    
    @Bean
    public GetGuestUseCase getGuestUseCase(GuestRepository guestRepository) {
        return new GetGuestUseCase(guestRepository);
    }
    
    @Bean
    public ListGuestsUseCase listGuestsUseCase(GuestRepository guestRepository) {
        return new ListGuestsUseCase(guestRepository);
    }
    
    @Bean
    public UpdateGuestUseCase updateGuestUseCase(GuestRepository guestRepository) {
        return new UpdateGuestUseCase(guestRepository);
    }
    
    // Room use cases
    
    @Bean
    public CreateRoomUseCase createRoomUseCase(RoomRepository roomRepository, 
                                              RoomTypeRepository roomTypeRepository) {
        return new CreateRoomUseCase(roomRepository, roomTypeRepository);
    }
    
    @Bean
    public ListRoomsUseCase listRoomsUseCase(RoomRepository roomRepository) {
        return new ListRoomsUseCase(roomRepository);
    }
    
    @Bean
    public FindAvailableRoomsUseCase findAvailableRoomsUseCase(RoomRepository roomRepository) {
        return new FindAvailableRoomsUseCase(roomRepository);
    }
    
    @Bean
    public CreateRoomTypeUseCase createRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        return new CreateRoomTypeUseCase(roomTypeRepository);
    }
    
    // Reservation use cases
    
    @Bean
    public CreateReservationUseCase createReservationUseCase(
            ReservationRepository reservationRepository,
            GuestRepository guestRepository,
            RoomRepository roomRepository) {
        return new CreateReservationUseCase(reservationRepository, guestRepository, roomRepository);
    }
    
    @Bean
    public ListReservationsUseCase listReservationsUseCase(ReservationRepository reservationRepository) {
        return new ListReservationsUseCase(reservationRepository);
    }
    
    @Bean
    public CheckInReservationUseCase checkInReservationUseCase(ReservationRepository reservationRepository) {
        return new CheckInReservationUseCase(reservationRepository);
    }
    
    @Bean
    public CheckOutReservationUseCase checkOutReservationUseCase(ReservationRepository reservationRepository) {
        return new CheckOutReservationUseCase(reservationRepository);
    }
}
