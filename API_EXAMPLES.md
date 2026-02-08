# API Examples - Gestion ERP

This document provides practical examples of API calls for the Gestion ERP microservice.

## Base URL

```
http://localhost:8081/api/v1
```

## Authentication

Currently, the service does not require authentication. Integration with auth-ERP will be added in future iterations.

---

## Guest Management

### Create a New Guest

**Request:**
```bash
curl -X POST http://localhost:8081/api/v1/guests \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-0123",
    "dateOfBirth": "1990-05-15",
    "nationality": "UNITED_STATES",
    "documentNumber": "PASS-US123456",
    "documentType": "PASSPORT"
  }'
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-0123",
  "dateOfBirth": "1990-05-15",
  "nationality": "UNITED_STATES",
  "documentNumber": "PASS-US123456",
  "documentType": "PASSPORT",
  "notes": null,
  "createdAt": "2026-02-07"
}
```

### Get Guest by ID

**Request:**
```bash
curl -X GET http://localhost:8081/api/v1/guests/550e8400-e29b-41d4-a716-446655440000
```

**Response:** `200 OK`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-0123",
  "dateOfBirth": "1990-05-15",
  "nationality": "UNITED_STATES",
  "documentNumber": "PASS-US123456",
  "documentType": "PASSPORT",
  "notes": null,
  "createdAt": "2026-02-07"
}
```

### List All Guests

**Request:**
```bash
curl -X GET http://localhost:8081/api/v1/guests
```

### Search Guests by Name

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/guests?search=John"
```

---

## Room Management

### Create a Room Type

**Request:**
```bash
curl -X POST http://localhost:8081/api/v1/room-types \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Deluxe Suite",
    "description": "Luxury suite with premium amenities",
    "maxOccupancy": 3,
    "basePrice": 150.00
  }'
```

### Create a Room

**Request:**
```bash
curl -X POST http://localhost:8081/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": 301,
    "roomTypeId": "rt-001",
    "amenities": ["BATHROOM", "WIFI", "TELEVISION", "BALCONY"],
    "numberOfBeds": 2
  }'
```

**Response:** `201 Created`
```json
{
  "id": "room-301",
  "roomNumber": 301,
  "roomType": {
    "id": "rt-001",
    "name": "Deluxe Suite",
    "description": "Luxury suite with premium amenities",
    "maxOccupancy": 3,
    "basePrice": 150.00
  },
  "roomStatus": "AVAILABLE",
  "amenities": ["BATHROOM", "WIFI", "TELEVISION", "BALCONY"],
  "bedCount": 2,
  "createdAt": "2026-02-07"
}
```

### List All Rooms

**Request:**
```bash
curl -X GET http://localhost:8081/api/v1/rooms
```

### Find Available Rooms

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/rooms/available?checkIn=2026-02-10&checkOut=2026-02-15"
```

### Find Available Rooms by Capacity

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/rooms/available?checkIn=2026-02-10&checkOut=2026-02-15&minCapacity=2"
```

---

## Reservation Management

### Create a Reservation

**Request:**
```bash
curl -X POST http://localhost:8081/api/v1/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "checkIn": "2026-02-10",
    "checkOut": "2026-02-15",
    "quotedAmount": 750.00,
    "source": "DIRECT",
    "guestPrincipalId": "guest-001",
    "roomId": "room-301",
    "additionalGuestIds": ["guest-002"]
  }'
```

**Response:** `201 Created`
```json
{
  "id": "res-001",
  "reservationCode": "RES-A1B2C3D4",
  "checkIn": "2026-02-10",
  "checkOut": "2026-02-15",
  "status": "CONFIRMED",
  "quotedAmount": 750.00,
  "source": "DIRECT",
  "guestPrincipalId": "guest-001",
  "guestPrincipalName": "John Doe",
  "roomId": "room-301",
  "roomNumber": 301,
  "createdAt": "2026-02-07"
}
```

### List All Reservations

**Request:**
```bash
curl -X GET http://localhost:8081/api/v1/reservations
```

### List Active Reservations

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/reservations?filter=active"
```

### List Reservations by Date Range

**Request:**
```bash
curl -X GET "http://localhost:8081/api/v1/reservations?startDate=2026-02-01&endDate=2026-02-28"
```

### Check-In a Reservation

**Request:**
```bash
curl -X POST http://localhost:8081/api/v1/reservations/res-001/check-in
```

**Response:** `200 OK`
```json
{
  "id": "res-001",
  "reservationCode": "RES-A1B2C3D4",
  "checkIn": "2026-02-10",
  "checkOut": "2026-02-15",
  "status": "CHECKED_IN",
  "quotedAmount": 750.00,
  "source": "DIRECT",
  "guestPrincipalId": "guest-001",
  "guestPrincipalName": "John Doe",
  "roomId": "room-301",
  "roomNumber": 301,
  "createdAt": "2026-02-07"
}
```

### Check-Out a Reservation

**Request:**
```bash
curl -X POST http://localhost:8081/api/v1/reservations/res-001/check-out
```

**Response:** `200 OK`
```json
{
  "id": "res-001",
  "reservationCode": "RES-A1B2C3D4",
  "checkIn": "2026-02-10",
  "checkOut": "2026-02-15",
  "status": "CHECKED_OUT",
  "quotedAmount": 750.00,
  "source": "DIRECT",
  "guestPrincipalId": "guest-001",
  "guestPrincipalName": "John Doe",
  "roomId": "room-301",
  "roomNumber": 301,
  "createdAt": "2026-02-07"
}
```

---

## Health Check

### Application Health

**Request:**
```bash
curl -X GET http://localhost:8081/actuator/health
```

**Response:** `200 OK`
```json
{
  "status": "UP"
}
```

---

## Error Responses

### Resource Not Found (404)

```json
{
  "status": 404,
  "error": "Resource Not Found",
  "message": "Guest not found with identifier: invalid-id",
  "path": "/api/v1/guests/invalid-id",
  "timestamp": 1707318000000
}
```

### Resource Already Exists (409)

```json
{
  "status": 409,
  "error": "Resource Already Exists",
  "message": "Guest already exists with identifier: john.doe@example.com",
  "path": "/api/v1/guests",
  "timestamp": 1707318000000
}
```

### Business Rule Violation (422)

```json
{
  "status": 422,
  "error": "Business Rule Violation",
  "message": "Room 301 is already reserved for the selected dates",
  "path": "/api/v1/reservations",
  "timestamp": 1707318000000
}
```

### Validation Error (400)

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "First name is required, Email is required",
  "path": "/api/v1/guests",
  "timestamp": 1707318000000
}
```

---

## Testing with Postman

A Postman collection is available in the project repository:
`postman/Gestion-ERP.postman_collection.json`

Import it to quickly test all endpoints.

---

## Integration with auth-ERP

Future integration will require Bearer token authentication:

```bash
curl -X GET http://localhost:8081/api/v1/guests \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```
