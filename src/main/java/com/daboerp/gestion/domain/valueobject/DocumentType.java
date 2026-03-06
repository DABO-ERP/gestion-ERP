package com.daboerp.gestion.domain.valueobject;

/**
 * Identification document types enumeration.
 */
public enum DocumentType {
    PASSPORT("Passport"),
    IDENTITY_CARD("Identity Card"),
    NATIONAL_ID("National ID"),
    CIVIL_REGISTRY("Civil Registry"),
    FOREIGN_ID("Foreigner ID");
    
    private final String displayName;
    
    DocumentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
