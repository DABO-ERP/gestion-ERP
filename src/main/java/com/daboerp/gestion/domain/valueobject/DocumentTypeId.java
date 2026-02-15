package com.daboerp.gestion.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Document type identifier value object.
 * Immutable and self-validating.
 */
public class DocumentTypeId {
    
    private final String value;
    
    private DocumentTypeId(String value) {
        Objects.requireNonNull(value, "Document type ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Document type ID cannot be blank");
        }
        this.value = value;
    }
    
    public static DocumentTypeId of(String value) {
        return new DocumentTypeId(value);
    }
    
    public static DocumentTypeId generate() {
        return new DocumentTypeId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentTypeId that = (DocumentTypeId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "DocumentTypeId{" +
                "value='" + value + '\'' +
                '}';
    }
}