package com.daboerp.gestion.application.usecase.reservation;

import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.entity.StatusType;
import com.daboerp.gestion.domain.repository.ReservationRepository;
import com.daboerp.gestion.domain.valueobject.Source;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Use case for filtering reservations with optional pagination.
 * Supports filtering by status, source, and stay dates.
 */
public class FilterReservationsUseCase {
    
    private final ReservationRepository reservationRepository;
    
    public FilterReservationsUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = Objects.requireNonNull(reservationRepository, "Reservation repository cannot be null");
    }
    
    /**
     * Execute reservation filtering without pagination.
     */
    public List<Reservation> execute(FilterCriteria criteria) {
        validateCriteria(criteria);
        return reservationRepository.findByFilters(
            criteria.status(),
            criteria.source(),
            criteria.checkInStart(),
            criteria.checkInEnd(),
            criteria.stayStart(),
            criteria.stayEnd()
        );
    }
    
    /**
     * Execute reservation filtering with pagination.
     */
    public PaginationResult executeWithPagination(FilterCriteria criteria, int page, int size) {
        validateCriteria(criteria);
        validatePaginationParams(page, size);
        
        List<Reservation> items = reservationRepository.findByFiltersWithPagination(
            criteria.status(),
            criteria.source(),
            criteria.checkInStart(),
            criteria.checkInEnd(),
            criteria.stayStart(),
            criteria.stayEnd(),
            page,
            size
        );
        
        long totalCount = reservationRepository.countByFilters(
            criteria.status(),
            criteria.source(),
            criteria.checkInStart(),
            criteria.checkInEnd(),
            criteria.stayStart(),
            criteria.stayEnd()
        );
        
        long totalPages = (totalCount + size - 1) / size; // Ceiling division
        
        return new PaginationResult(items, page, size, totalPages, totalCount);
    }
    
    private void validateCriteria(FilterCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Filter criteria cannot be null");
        }
        
        // Validate date ranges
        if (criteria.checkInStart() != null && criteria.checkInEnd() != null) {
            if (criteria.checkInStart().isAfter(criteria.checkInEnd())) {
                throw new IllegalArgumentException("Check-in start date must be before or equal to check-in end date");
            }
        }
        
        if (criteria.stayStart() != null && criteria.stayEnd() != null) {
            if (criteria.stayStart().isAfter(criteria.stayEnd())) {
                throw new IllegalArgumentException("Stay start date must be before or equal to stay end date");
            }
        }
    }
    
    private void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
    }
    
    /**
     * Filter criteria for reservation queries.
     */
    public record FilterCriteria(
        StatusType status,
        Source source,
        LocalDate checkInStart,
        LocalDate checkInEnd,
        LocalDate stayStart,
        LocalDate stayEnd
    ) {
        public static FilterCriteria empty() {
            return new FilterCriteria(null, null, null, null, null, null);
        }
        
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private StatusType status;
            private Source source;
            private LocalDate checkInStart;
            private LocalDate checkInEnd;
            private LocalDate stayStart;
            private LocalDate stayEnd;
            
            public Builder status(StatusType status) {
                this.status = status;
                return this;
            }
            
            public Builder source(Source source) {
                this.source = source;
                return this;
            }
            
            public Builder checkInStart(LocalDate checkInStart) {
                this.checkInStart = checkInStart;
                return this;
            }
            
            public Builder checkInEnd(LocalDate checkInEnd) {
                this.checkInEnd = checkInEnd;
                return this;
            }
            
            public Builder stayStart(LocalDate stayStart) {
                this.stayStart = stayStart;
                return this;
            }
            
            public Builder stayEnd(LocalDate stayEnd) {
                this.stayEnd = stayEnd;
                return this;
            }
            
            public FilterCriteria build() {
                return new FilterCriteria(status, source, checkInStart, checkInEnd, stayStart, stayEnd);
            }
        }
    }
    
    /**
     * Result object for paginated queries.
     */
    public record PaginationResult(
        List<Reservation> items,
        int currentPage,
        int pageSize,
        long totalPages,
        long totalCount
    ) {}
}
