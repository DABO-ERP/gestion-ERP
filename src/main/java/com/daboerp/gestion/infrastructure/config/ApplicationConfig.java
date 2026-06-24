package com.daboerp.gestion.infrastructure.config;

import com.daboerp.gestion.application.command.CommandHandler;
import com.daboerp.gestion.application.command.guest.CreateGuestCommand;
import com.daboerp.gestion.application.decorator.LoggingCommandHandlerDecorator;
import com.daboerp.gestion.application.decorator.ValidationCommandHandlerDecorator;
import com.daboerp.gestion.application.usecase.documenttype.*;
import com.daboerp.gestion.application.usecase.guest.*;
import com.daboerp.gestion.application.usecase.payment.ListAllPaymentsUseCase;
import com.daboerp.gestion.application.usecase.payment.ListPaymentsUseCase;
import com.daboerp.gestion.application.usecase.payment.RegisterPaymentUseCase;
import com.daboerp.gestion.application.usecase.payment.VoidPaymentUseCase;
import com.daboerp.gestion.application.usecase.reservation.*;
import com.daboerp.gestion.application.usecase.room.*;
import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.event.DomainEventPublisher;
import com.daboerp.gestion.domain.factory.guest.GuestFactory;
import com.daboerp.gestion.domain.repository.*;
import com.daboerp.gestion.domain.repository.PaymentRepository;
import com.daboerp.gestion.domain.strategy.pricing.*;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
            new PeakSeasonPricingStrategy(),
            new LongStayPricingStrategy(),
            new StandardPricingStrategy()
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

    @Bean
    public DeleteGuestUseCase deleteGuestUseCase(GuestRepository guestRepository) {
        return new DeleteGuestUseCase(guestRepository);
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
    public UpdateRoomStatusUseCase updateRoomStatusUseCase(RoomRepository roomRepository) {
        return new UpdateRoomStatusUseCase(roomRepository);
    }

    @Bean
    public CreateRoomTypeUseCase createRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        return new CreateRoomTypeUseCase(roomTypeRepository);
    }

    @Bean
    public ListRoomTypesUseCase listRoomTypesUseCase(RoomTypeRepository roomTypeRepository) {
        return new ListRoomTypesUseCase(roomTypeRepository);
    }

    @Bean
    public UpdateRoomTypeUseCase updateRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        return new UpdateRoomTypeUseCase(roomTypeRepository);
    }

    @Bean
    public DeleteRoomTypeUseCase deleteRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        return new DeleteRoomTypeUseCase(roomTypeRepository);
    }

    @Bean
    public UpdateRoomUseCase updateRoomUseCase(RoomRepository roomRepository,
                                              RoomTypeRepository roomTypeRepository) {
        return new UpdateRoomUseCase(roomRepository, roomTypeRepository);
    }

    @Bean
    public DeleteRoomUseCase deleteRoomUseCase(RoomRepository roomRepository) {
        return new DeleteRoomUseCase(roomRepository);
    }

    @Bean
    public ReactivateRoomUseCase reactivateRoomUseCase(RoomRepository roomRepository,
                                                      RoomTypeRepository roomTypeRepository) {
        return new ReactivateRoomUseCase(roomRepository, roomTypeRepository);
    }

    @Bean
    public RestoreRoomUseCase restoreRoomUseCase(RoomRepository roomRepository) {
        return new RestoreRoomUseCase(roomRepository);
    }

    // Reservation use cases

    @Bean
    public CreateReservationUseCase createReservationUseCase(
            ReservationRepository reservationRepository,
            GuestRepository guestRepository,
            RoomRepository roomRepository,
            PricingContext pricingContext) {
        return new CreateReservationUseCase(reservationRepository, guestRepository, roomRepository, pricingContext);
    }

    @Bean
    public ListReservationsUseCase listReservationsUseCase(ReservationRepository reservationRepository) {
        return new ListReservationsUseCase(reservationRepository);
    }

    @Bean
    public CheckInReservationUseCase checkInReservationUseCase(ReservationRepository reservationRepository,
                                                              RoomRepository roomRepository) {
        return new CheckInReservationUseCase(reservationRepository, roomRepository);
    }

    @Bean
    public CheckOutReservationUseCase checkOutReservationUseCase(ReservationRepository reservationRepository,
                                                                RoomRepository roomRepository) {
        return new CheckOutReservationUseCase(reservationRepository, roomRepository);
    }

    @Bean
    public FilterReservationsUseCase filterReservationsUseCase(ReservationRepository reservationRepository) {
        return new FilterReservationsUseCase(reservationRepository);
    }

    @Bean
    public GetReservationUseCase getReservationUseCase(ReservationRepository reservationRepository) {
        return new GetReservationUseCase(reservationRepository);
    }

    @Bean
    public UpdateReservationUseCase updateReservationUseCase(ReservationRepository reservationRepository,
                                                             GuestRepository guestRepository,
                                                             RoomRepository roomRepository) {
        return new UpdateReservationUseCase(reservationRepository, guestRepository, roomRepository);
    }

    @Bean
    public CancelReservationUseCase cancelReservationUseCase(ReservationRepository reservationRepository,
                                                             RoomRepository roomRepository) {
        return new CancelReservationUseCase(reservationRepository, roomRepository);
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

    // Amenity Definition use cases

    @Bean
    public ListAmenityDefinitionsUseCase listAmenityDefinitionsUseCase(AmenityDefinitionRepository amenityDefinitionRepository) {
        return new ListAmenityDefinitionsUseCase(amenityDefinitionRepository);
    }

    @Bean
    public CreateAmenityDefinitionUseCase createAmenityDefinitionUseCase(AmenityDefinitionRepository amenityDefinitionRepository) {
        return new CreateAmenityDefinitionUseCase(amenityDefinitionRepository);
    }

    @Bean
    public DeleteAmenityDefinitionUseCase deleteAmenityDefinitionUseCase(AmenityDefinitionRepository amenityDefinitionRepository,
                                                                         RoomRepository roomRepository) {
        return new DeleteAmenityDefinitionUseCase(amenityDefinitionRepository, roomRepository);
    }

    // Room Block use cases

    @Bean
    public BlockRoomUseCase blockRoomUseCase(RoomBlockRepository roomBlockRepository,
                                             RoomRepository roomRepository) {
        return new BlockRoomUseCase(roomBlockRepository, roomRepository);
    }

    @Bean
    public UnblockRoomUseCase unblockRoomUseCase(RoomBlockRepository roomBlockRepository) {
        return new UnblockRoomUseCase(roomBlockRepository);
    }

    @Bean
    public GetRoomBlocksUseCase getRoomBlocksUseCase(RoomBlockRepository roomBlockRepository) {
        return new GetRoomBlocksUseCase(roomBlockRepository);
    }

    @Bean
    public GetAllBlocksUseCase getAllBlocksUseCase(RoomBlockRepository roomBlockRepository) {
        return new GetAllBlocksUseCase(roomBlockRepository);
    }

    // Payment use cases

    @Bean
    public RegisterPaymentUseCase registerPaymentUseCase(PaymentRepository paymentRepository,
                                                         ReservationRepository reservationRepository) {
        return new RegisterPaymentUseCase(paymentRepository, reservationRepository);
    }

    @Bean
    public ListPaymentsUseCase listPaymentsUseCase(PaymentRepository paymentRepository) {
        return new ListPaymentsUseCase(paymentRepository);
    }

    @Bean
    public VoidPaymentUseCase voidPaymentUseCase(PaymentRepository paymentRepository,
                                                  ReservationRepository reservationRepository) {
        return new VoidPaymentUseCase(paymentRepository, reservationRepository);
    }

    @Bean
    public ListAllPaymentsUseCase listAllPaymentsUseCase(PaymentRepository paymentRepository) {
        return new ListAllPaymentsUseCase(paymentRepository);
    }
}
