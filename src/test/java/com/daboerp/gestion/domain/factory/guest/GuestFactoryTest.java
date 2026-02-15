package com.daboerp.gestion.domain.factory.guest;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GuestFactory - Factory Pattern validation.
 */
class GuestFactoryTest {
    
    private GuestFactory guestFactory;
    
    @BeforeEach
    void setUp() {
        guestFactory = new GuestFactory();
    }
    
    @Test
    void shouldCreateValidGuest() {
        // Given
        GuestFactory.GuestBuilder builder = GuestFactory.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("+1234567890")
            .dateOfBirth(LocalDate.of(1990, 1, 15))
            .nationality(Nationality.UNITED_STATES)
            .documentNumber("PASSPORT123")
            .documentType(DocumentType.PASSPORT);
        
        // When
        Guest guest = guestFactory.create(builder);
        
        // Then
        assertNotNull(guest);
        assertEquals("John", guest.getFirstName());
        assertEquals("Doe", guest.getLastName());
        assertEquals("john.doe@example.com", guest.getEmail());
        assertNotNull(guest.getId());
        assertFalse(guest.getDomainEvents().isEmpty());
    }
    
    @Test
    void shouldValidateMinimumAge() {
        // Given
        GuestFactory.GuestBuilder builder = GuestFactory.builder()
            .firstName("Young")
            .lastName("Guest")
            .email("young@example.com")
            .dateOfBirth(LocalDate.now().minusYears(15)) // Too young
            .nationality(Nationality.UNITED_STATES)
            .documentNumber("ID123")
            .documentType(DocumentType.NATIONAL_ID);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> guestFactory.create(builder)
        );
        
        assertTrue(exception.getMessage().contains("must be at least 16 years old"));
    }
    
    @Test
    void shouldValidateEmailFormat() {
        // Given
        GuestFactory.GuestBuilder builder = GuestFactory.builder()
            .firstName("John")
            .lastName("Doe")
            .email("invalid-email") // Invalid format
            .nationality(Nationality.UNITED_STATES)
            .documentNumber("ID123")
            .documentType(DocumentType.NATIONAL_ID);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> guestFactory.create(builder)
        );
        
        assertTrue(exception.getMessage().contains("Invalid email format"));
    }
    
    @Test
    void shouldValidatePassportLength() {
        // Given
        GuestFactory.GuestBuilder builder = GuestFactory.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john@example.com")
            .nationality(Nationality.UNITED_STATES)
            .documentNumber("ABC") // Too short for passport
            .documentType(DocumentType.PASSPORT);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> guestFactory.create(builder)
        );
        
        assertTrue(exception.getMessage().contains("Passport number must be between 6 and 12 characters"));
    }
}