package com.daboerp.gestion.domain.valueobject;

/**
 * Room amenities enumeration.
 */
public enum Amenity {
    BATHROOM("Bathroom"),
    TELEVISION("Television"),
    SOFA("Sofa"),
    BALCONY("Balcony"),
    AIR_CONDITIONING("Air Conditioning"),
    WIFI("WiFi"),
    MINI_BAR("Mini Bar"),
    SAFE("Safe"),
    DESK("Desk"),
    WARDROBE("Wardrobe");
    
    private final String displayName;
    
    Amenity(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
