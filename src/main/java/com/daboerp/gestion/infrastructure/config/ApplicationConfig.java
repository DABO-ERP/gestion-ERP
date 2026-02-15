package com.daboerp.gestion.infrastructure.config;

import com.daboerp.gestion.application.command.CommandHandler;
import com.daboerp.gestion.application.command.guest.CreateGuestCommand;
import com.daboerp.gestion.application.decorator.LoggingCommandHandlerDecorator;
import com.daboerp.gestion.application.decorator.ValidationCommandHandlerDecorator;
import com.daboerp.gestion.application.usecase.documenttype.*;
import com.daboerp.gestion.application.usecase.guest.*;
import com.daboerp.gestion.application.usecase.reservation.*;
import com.daboerp.gestion.application.usecase.room.*;
import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.event.DomainEventPublisher;
import com.daboerp.gestion.domain.factory.guest.GuestFactory;
import com.daboerp.gestion.domain.repository.DocumentTypeRepository;
import com.daboerp.gestion.domain.repository.GuestRepository;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.repository.RoomRepository;
import com.daboerp.gestion.domain.repository.RoomTypeRepository;
import com.daboerp.gestion.domain.strategy.pricing.*;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Application configuration - wires use cases with dependencies.
 * Dependency Injection configuration for Clean Architecture.
 */
@Configuration
public class ApplicationConfig {
    
    // Guest use cases
    
    @Bean
    public CreateGuestUseCase createGuestUseCase(GuestRepository guestRepository, 
                                               DomainEventPublisher eventPublisher) {
        return new CreateGuestUseCase(guestRepository, eventPublisher);
    }
    
    @Bean
    public CommandHandler<CreateGuestCommand, Guest> createGuestCommandHandler(GuestRepository guestRepository, 
                                               DomainEventPublisher eventPublisher,
                                               Validator validator) {
        CreateGuestUseCase baseUseCase = new CreateGuestUseCase(guestRepository, eventPublisher);
        
        // Apply decorators using Decorator Pattern
        var validationDecorator = new ValidationCommandHandlerDecorator<>(baseUseCase, validator);
        return new LoggingCommandHandlerDecorator<CreateGuestCommand, Guest>(validationDecorator);
    }
    
    // Domain Factories
    
    @Bean
    public GuestFactory guestFactory() {
        return new GuestFactory();
    }
    
    // Strategy Pattern Configuration for Pricing
    
    @Bean
    public PricingContext pricingContext() {
        List<PricingStrategy> strategies = List.of(
            new PeakSeasonPricingStrategy(),    // Highest priority (10)
            new LongStayPricingStrategy(),      // Medium priority (5)
            new StandardPricingStrategy()       // Lowest priority (1) - fallback
        );
        return new PricingContext(strategies);
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
    
    @Bean
    public FilterReservationsUseCase filterReservationsUseCase(ReservationRepository reservationRepository) {
        return new FilterReservationsUseCase(reservationRepository);
    }
    
    // Document Type use cases
    
    @Bean
    public ListDocumentTypesUseCase listDocumentTypesUseCase(DocumentTypeRepository documentTypeRepository) {
        return new ListDocumentTypesUseCase(documentTypeRepository);
    }
    
    @Bean
    public GetDocumentTypeUseCase getDocumentTypeUseCase(DocumentTypeRepository documentTypeRepository) {
        return new GetDocumentTypeUseCase(documentTypeRepository);
    }
    
    @Bean
    public CreateDocumentTypeUseCase createDocumentTypeUseCase(DocumentTypeRepository documentTypeRepository) {
        return new CreateDocumentTypeUseCase(documentTypeRepository);
    }
    
    @Bean
    public SearchDocumentTypesUseCase searchDocumentTypesUseCase(DocumentTypeRepository documentTypeRepository) {
        return new SearchDocumentTypesUseCase(documentTypeRepository);
    }
    
    @Bean
    public ListDocumentTypesWithPaginationUseCase listDocumentTypesWithPaginationUseCase(DocumentTypeRepository documentTypeRepository) {
        return new ListDocumentTypesWithPaginationUseCase(documentTypeRepository);
    }
}
