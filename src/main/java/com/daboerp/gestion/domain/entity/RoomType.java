package com.daboerp.gestion.domain.entity;

import com.daboerp.gestion.domain.valueobject.RoomTypeId;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * RoomType entity - defines category and pricing of rooms.
 * Composed by Room.
 */
public class RoomType {
    
    private final RoomTypeId id;
    private String name;
    private String description;
    private int maxOccupancy;
    private BigDecimal basePrice;
    
    private RoomType(RoomTypeId id, String name, String description, int maxOccupancy, BigDecimal basePrice) {
        this.id = Objects.requireNonNull(id, "Room type ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.maxOccupancy = maxOccupancy;
        this.basePrice = Objects.requireNonNull(basePrice, "Base price cannot be null");
        
        validateMaxOccupancy(maxOccupancy);
        validateBasePrice(basePrice);
    }
    
    public static RoomType create(String name, String description, int maxOccupancy, BigDecimal basePrice) {
        RoomTypeId id = RoomTypeId.generate();
        return new RoomType(id, name, description, maxOccupancy, basePrice);
    }
    
    public static RoomType reconstitute(RoomTypeId id, String name, String description, 
                                       int maxOccupancy, BigDecimal basePrice) {
        return new RoomType(id, name, description, maxOccupancy, basePrice);
    }
    
    public void updatePricing(BigDecimal newBasePrice) {
        validateBasePrice(newBasePrice);
        this.basePrice = newBasePrice;
    }
    
    public void updateDetails(String name, String description, int maxOccupancy) {
        Objects.requireNonNull(name, "Name cannot be null");
        validateMaxOccupancy(maxOccupancy);
        this.name = name;
        this.description = description;
        this.maxOccupancy = maxOccupancy;
    }
    
    private void validateMaxOccupancy(int maxOccupancy) {
        if (maxOccupancy <= 0) {
            throw new IllegalArgumentException("Max occupancy must be positive");
        }
    }
    
    private void validateBasePrice(BigDecimal basePrice) {
        if (basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
    }
    
    public RoomTypeId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomType roomType = (RoomType) o;
        return Objects.equals(id, roomType.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
