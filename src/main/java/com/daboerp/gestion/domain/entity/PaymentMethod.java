package com.daboerp.gestion.domain.entity;

/**
 * Enumeration of supported payment methods.
 */
public enum PaymentMethod {
    CASH("Cash"),
    CARD("Card"),
    TRANSFER("Transfer"),
    OTHER("Other");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
