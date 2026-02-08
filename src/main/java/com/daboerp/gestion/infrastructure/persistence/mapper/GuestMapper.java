package com.daboerp.gestion.infrastructure.persistence.mapper;

import com.daboerp.gestion.domain.entity.Guest;
import com.daboerp.gestion.domain.entity.LevelNote;
import com.daboerp.gestion.domain.entity.Notes;
import com.daboerp.gestion.domain.valueobject.GuestId;
import com.daboerp.gestion.infrastructure.persistence.entity.GuestJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Guest domain entity and JPA entity.
 * Handles conversion between domain and infrastructure layers.
 */
@Component
public class GuestMapper {
    
    public GuestJpaEntity toJpaEntity(Guest guest) {
        GuestJpaEntity entity = new GuestJpaEntity();
        entity.setId(guest.getId().getValue());
        entity.setFirstName(guest.getFirstName());
        entity.setLastName(guest.getLastName());
        entity.setEmail(guest.getEmail());
        entity.setPhone(guest.getPhone());
        entity.setDateOfBirth(guest.getDateOfBirth());
        entity.setNationality(guest.getNationality());
        entity.setDocumentNumber(guest.getDocumentNumber());
        entity.setDocumentType(guest.getDocumentType());
        entity.setCreatedAt(guest.getCreatedAt());
        
        // Map notes if present
        if (guest.getNotes() != null) {
            entity.setNotesId(guest.getNotes().getId());
            entity.setNotesText(guest.getNotes().getText());
            entity.setNotesLevel(guest.getNotes().getLevel().name());
        }
        
        return entity;
    }
    
    public Guest toDomainEntity(GuestJpaEntity entity) {
        Notes notes = null;
        if (entity.getNotesId() != null && entity.getNotesText() != null && entity.getNotesLevel() != null) {
            notes = Notes.reconstitute(
                entity.getNotesId(),
                entity.getNotesText(),
                LevelNote.valueOf(entity.getNotesLevel())
            );
        }
        
        return Guest.reconstitute(
            GuestId.of(entity.getId()),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getDateOfBirth(),
            entity.getNationality(),
            entity.getDocumentNumber(),
            entity.getDocumentType(),
            notes,
            entity.getCreatedAt()
        );
    }
}
