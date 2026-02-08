---
name: domain-model-documentation
description: Use this skill when building or reviewing the domain model for the hotel/hostel management system. This includes defining entities, attributes, relationships, and enumerations based on hospitality domain expertise.
---

# Domain Model Documentation

## Entities

### Guest
Represents a guest in the hostel management system.

**Attributes:**
- `id: String` - Unique identifier
- `firstName: String` - Guest's first name
- `lastName: String` - Guest's last name
- `email: String` - Contact email address
- `phone: String` - Contact phone number
- `dateOfBirth: LocalDate` - Date of birth
- `notes: String` - Additional notes about the guest
- `nationality: Nationality` - Guest's nationality (enum)
- `documentNumber: String` - Identification document number
- `documentType: DocumentType` - Type of identification document (enum)
- `notes: Notes` - Structured notes with severity levels

**Relationships:**
- Has a `Nationality` (enumeration)
- Has a `DocumentType` (enumeration)
- Contains `Notes` entity (composition)
- Referenced by `Reservation` as principal guest or in guest list

---

### Room
Represents a physical room in the hostel.

**Attributes:**
- `id: String` - Unique identifier
- `roomNumber: Integer` - Room number
- `roomType: RoomType` - Type/category of the room
- `roomStatus: RoomStatus` - Current operational status (enum)
- `amenities: List<Amenity>` - List of available amenities (enum)

**Relationships:**
- Has one `RoomType` (composition)
- Has one `RoomStatus` (enumeration)
- Contains multiple `Amenity` (enumeration)
- Contains multiple `Bed` entities (composition)
- Referenced by `Reservation`

---

### RoomType
Defines the category and pricing of rooms.

**Attributes:**
- `id: String` - Unique identifier
- `name: String` - Type name
- `description: String` - Detailed description
- `maxOccupancy: int` - Maximum number of guests
- `basePrice: BigDecimal` - Base price per night

**Relationships:**
- Composed by `Room`

---

### Bed
Represents an individual bed within a room.

**Attributes:**
- `id: String` - Unique identifier
- `bedNumber: Integer` - Bed identifier within the room
- `room: Room` - Reference to parent room

**Relationships:**
- Belongs to one `Room` (aggregation)

---

### Notes
Structured notes with severity classification.

**Attributes:**
- `id: String` - Unique identifier
- `text: String` - Note content
- `level: LevelNote` - Severity/importance level (enum)

**Relationships:**
- Has one `LevelNote` (composition)
- Composed by `Guest`

---

### Reservation
Represents a booking made by a guest.

**Attributes:**
- `id: String` - Unique identifier
- `reservationCode: String` - Unique reservation code
- `checkIn: LocalDate` - Check-in date
- `checkOut: LocalDate` - Check-out date
- `status: ReservationStatus` - Current reservation status
- `quotedAmount: BigDecimal` - Total quoted price
- `source: Source` - Booking channel/source (enum)
- `createdAt: LocalDate` - Reservation creation date
- `guestPrincipal: Guest` - Main guest responsible for the reservation
- `guests: List<Guest>` - All guests included in the reservation
- `room: Room` - Assigned room
- `stay: Stay` - Associated stay information

**Relationships:**
- References one `Guest` as principal guest
- Contains multiple `Guest` references
- References one `Room`
- Has one `ReservationStatus` (composition)
- Has one `Source` (enumeration)
- Contains one `Stay` (composition)

---

### ReservationStatus
Tracks the current state of a reservation.

**Attributes:**
- `id: String` - Unique identifier
- `statusType: StatusType` - Type of status
- `note: String` - Additional status notes

**Relationships:**
- Contains one `StatusType` (composition)
- Composed by `Reservation`

---

### StatusType
Defines available reservation status types.

**Attributes:**
- `id: String` - Unique identifier
- `name: String` - Status name

**Relationships:**
- Composed by `ReservationStatus`

---

### Stay
Represents the actual stay period derived from a reservation.

**Attributes:**
- `id: String` - Unique identifier
- `checkIn: LocalDate` - Actual check-in date
- `checkOut: LocalDate` - Actual check-out date

**Relationships:**
- Composed by `Reservation`

---

## Enumerations

### Nationality
Defines guest nationalities:
- `COLOMBIA`
- `UNITED_STATES`
- `SPAIN`
- `CHINA`
- `...` (extensible)

---

### DocumentType
Identification document types:
- `PASSPORT` - Passport
- `IDENTITY_CARD` - Identity card
- `NATIONAL_ID` - National ID
- `CIVIL_REGISTRY` - Civil registry
- `FOREIGN_ID` - Foreigner ID

---

### RoomStatus
Room operational states:
- `AVAILABLE` - Available
- `OCCUPIED` - Occupied
- `OUT_OF_SERVICE` - Out of service

---

### Amenity
Room amenities:
- `BATHROOM` - Bathroom
- `TELEVISION` - Television
- `SOFA` - Sofa
- `BALCONY` - Balcony

---

### LevelNote
Note severity levels:
- `WARNING` - Warning
- `NORMAL` - Normal
- `INFORMATIONAL` - Informational

---

### Source
Reservation booking sources:
- `DIRECT` - Direct booking
- `BOOKING` - Booking.com
- `HOSTELWORLD` - Hostelworld

---

## Key Relationships Summary

1. **Guest → Reservation**: A guest can be the principal guest of a reservation and can be included in multiple reservations (1:N)
2. **Reservation → Room**: Each reservation is assigned to one room (N:1)
3. **Room → RoomType**: Each room has one type that defines its characteristics and pricing (N:1, composition)
4. **Room → Bed**: A room contains multiple beds (1:N, composition)
5. **Reservation → Stay**: Each reservation generates one stay record with actual dates (1:1, composition)
6. **Guest → Notes**: Guests can have structured notes with severity levels (1:1, composition)
7. **Reservation → ReservationStatus**: Each reservation has a status composed of a status type and notes (1:1, composition)
8. **ReservationStatus → StatusType**: Status contains a type definition (1:1, composition)
9. **Notes → LevelNote**: Each note has a severity level classification (1:1, composition)
10. **Room → RoomStatus**: Each room has an operational status (N:1, enumeration)
11. **Room → Amenity**: A room can have multiple amenities (1:N, enumeration)
12. **Guest → Nationality**: Each guest has a nationality (N:1, enumeration)
13. **Guest → DocumentType**: Each guest has a document type for identification (N:1, enumeration)
14. **Reservation → Source**: Each reservation originates from a booking source (N:1, enumeration)
