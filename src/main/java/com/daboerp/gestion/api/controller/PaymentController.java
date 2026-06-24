package com.daboerp.gestion.api.controller;

import com.daboerp.gestion.api.dto.PaymentResponse;
import com.daboerp.gestion.api.dto.PaymentSummaryResponse;
import com.daboerp.gestion.api.dto.RegisterPaymentRequest;
import com.daboerp.gestion.application.usecase.payment.ListAllPaymentsUseCase;
import com.daboerp.gestion.application.usecase.payment.ListPaymentsUseCase;
import com.daboerp.gestion.application.usecase.payment.RegisterPaymentUseCase;
import com.daboerp.gestion.application.usecase.payment.VoidPaymentUseCase;
import com.daboerp.gestion.domain.entity.Payment;
import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.ReservationId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Payment Management", description = "APIs for managing payments associated with reservations")
public class PaymentController {

    private final RegisterPaymentUseCase registerPaymentUseCase;
    private final ListPaymentsUseCase listPaymentsUseCase;
    private final ListAllPaymentsUseCase listAllPaymentsUseCase;
    private final VoidPaymentUseCase voidPaymentUseCase;
    private final ReservationRepository reservationRepository;

    public PaymentController(RegisterPaymentUseCase registerPaymentUseCase,
                             ListPaymentsUseCase listPaymentsUseCase,
                             ListAllPaymentsUseCase listAllPaymentsUseCase,
                             VoidPaymentUseCase voidPaymentUseCase,
                             ReservationRepository reservationRepository) {
        this.registerPaymentUseCase = registerPaymentUseCase;
        this.listPaymentsUseCase = listPaymentsUseCase;
        this.listAllPaymentsUseCase = listAllPaymentsUseCase;
        this.voidPaymentUseCase = voidPaymentUseCase;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("/api/v1/reservations/{reservationId}/payments")
    @Operation(summary = "Register a payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment registered successfully",
            content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<PaymentResponse> registerPayment(
            @Parameter(description = "Reservation ID") @PathVariable String reservationId,
            @Valid @RequestBody RegisterPaymentRequest request) {
        var command = new RegisterPaymentUseCase.RegisterPaymentCommand(
            reservationId, request.amount(), request.method(), request.note()
        );
        Payment payment = registerPaymentUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(payment));
    }

    @GetMapping("/api/v1/reservations/{reservationId}/payments")
    @Operation(summary = "List payments for a reservation")
    public ResponseEntity<List<PaymentResponse>> listPayments(
            @Parameter(description = "Reservation ID") @PathVariable String reservationId) {
        var command = new ListPaymentsUseCase.ListPaymentsCommand(reservationId);
        List<PaymentResponse> response = listPaymentsUseCase.execute(command).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/v1/reservations/{reservationId}/payments/{paymentId}")
    @Operation(summary = "Void a payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Payment voided successfully"),
        @ApiResponse(responseCode = "404", description = "Payment or reservation not found")
    })
    public ResponseEntity<Void> voidPayment(
            @Parameter(description = "Reservation ID") @PathVariable String reservationId,
            @Parameter(description = "Payment ID") @PathVariable String paymentId) {
        var command = new VoidPaymentUseCase.VoidPaymentCommand(paymentId, reservationId);
        voidPaymentUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/payments")
    @Operation(summary = "List all payments across all reservations")
    public ResponseEntity<List<PaymentSummaryResponse>> listAllPayments() {
        List<Payment> payments = listAllPaymentsUseCase.execute();
        List<PaymentSummaryResponse> response = payments.stream()
            .map(this::toSummaryResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
            payment.getId().getValue(),
            payment.getReservationId().getValue(),
            payment.getAmount(),
            payment.getMethod().name(),
            payment.getPaidAt(),
            payment.getNote(),
            payment.getCreatedAt()
        );
    }

    private PaymentSummaryResponse toSummaryResponse(Payment payment) {
        String reservationCode = null;
        String guestName = null;

        var optReservation = reservationRepository.findById(
            ReservationId.of(payment.getReservationId().getValue())
        );
        if (optReservation.isPresent()) {
            Reservation reservation = optReservation.get();
            reservationCode = reservation.getReservationCode();
            guestName = reservation.getPrincipalGuest() != null
                ? reservation.getPrincipalGuest().getFullName()
                : null;
        }

        return new PaymentSummaryResponse(
            payment.getId().getValue(),
            payment.getReservationId().getValue(),
            reservationCode,
            guestName,
            payment.getAmount(),
            payment.getMethod().name(),
            payment.getPaidAt(),
            payment.getNote(),
            payment.getCreatedAt()
        );
    }
}
