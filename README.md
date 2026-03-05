# Gestion ERP - Hostel/Hotel Management Microservice

## Overview

Gestion ERP is a production-ready microservice for hostel and hotel management, built following **Clean Architecture** principles. It provides core management features including guest management, room management, and reservation management.

## Architecture

This project follows Clean Architecture (Hexagonal Architecture) with strict layer separation:

```
┌─────────────────────────────────────────┐
│           API Layer                     │
│  (Controllers, DTOs, Exception Handler) │
└──────────────┬──────────────────────────┘
               │ depends on
┌──────────────▼──────────────────────────┐
│      Application Layer                  │
│   (Use Cases, Commands, Exceptions)     │
└──────────────┬──────────────────────────┘
               │ depends on
┌──────────────▼──────────────────────────┐
│        Domain Layer                     │
│  (Entities, Value Objects, Repositories)│
└──────────────▲──────────────────────────┘
               │ implements
┌──────────────┴──────────────────────────┐
│      Infrastructure Layer               │
│  (JPA, Security, PostgreSQL)            │
└─────────────────────────────────────────┘
```

### Key Principles

- **Domain layer** has NO external dependencies
- **Application layer** depends ONLY on domain
- **Infrastructure** implements domain interfaces
- **API layer** orchestrates use cases

## Features

### Core Management

- **Guest Management**: Create, read, update guest information with document validation
- **Room Management**: Manage rooms, room types, beds, and amenities
- **Reservation Management**: Create reservations, check-in/check-out, status tracking
- **Availability Management**: Find available rooms by date range and capacity

### Domain Model

- **Entities**: Guest, Room, RoomType, Bed, Reservation, ReservationStatus, Stay
- **Value Objects**: GuestId, RoomId, ReservationId, Nationality, DocumentType, Source
- **Enumerations**: RoomStatus, Amenity, LevelNote, StatusType

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.10**
- **PostgreSQL 16**
- **Gradle 8.6**
- **Docker & Docker Compose**

## Getting Started

### Prerequisites

- Java 17+
- Docker & Docker Compose
- PostgreSQL 16 (or use Docker)

### Quick Start

1. **Clone the repository**
   ```bash
   cd gestion-ERP
   ```

2. **Configure environment**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Start with Docker Compose**
   ```bash
   docker-compose up -d
   ```

4. **OR Start for development**
   ```bash
   chmod +x dev-start.sh
   ./dev-start.sh
   ```

5. **Access the application**
   - API: http://localhost:8081
   - Health: http://localhost:8081/actuator/health

### Manual Setup

1. **Start PostgreSQL**
   ```bash
   docker-compose up -d postgres
   ```

2. **Run database migrations**
   ```bash
   psql -U gestion_user -d gestion_erp < db/migrations/V001__initial_schema.sql
   psql -U gestion_user -d gestion_erp < db/migrations/V002__seed_data.sql
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

## API Endpoints

### Guest Management

- `POST /api/v1/guests` - Create a new guest
- `GET /api/v1/guests/{id}` - Get guest by ID
- `GET /api/v1/guests?search={name}` - List/search guests

### Room Management

- `POST /api/v1/rooms` - Create a new room
- `GET /api/v1/rooms` - List all rooms
- `GET /api/v1/rooms/available?checkIn={date}&checkOut={date}` - Find available rooms

### Reservation Management

- `POST /api/v1/reservations` - Create a new reservation
- `GET /api/v1/reservations` - List reservations
- `GET /api/v1/reservations?filter=active` - List active reservations
- `POST /api/v1/reservations/{id}/check-in` - Check-in a reservation
- `POST /api/v1/reservations/{id}/check-out` - Check-out a reservation

## Testing

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Verify build
./gradlew verify
```

## Building

```bash
# Build JAR
./gradlew bootJar

# Build Docker image
docker build -t gestion-erp:latest .
```

## Project Structure

```
gestion-ERP/
├── src/main/java/com/daboerp/gestion/
│   ├── domain/              # Domain layer (entities, value objects, repositories)
│   ├── application/         # Application layer (use cases, commands)
│   ├── infrastructure/      # Infrastructure layer (JPA, persistence)
│   └── api/                 # API layer (controllers, DTOs)
├── src/main/resources/      # Configuration files
├── db/migrations/           # Database migration scripts
├── docker-compose.yml       # Docker Compose configuration
├── Dockerfile              # Docker image definition
└── build.gradle            # Gradle build configuration
```

## Configuration

Application configuration is managed through `application.yml` files:

- `application.yml` - Base configuration
- `application-dev.yml` - Development profile
- `application-test.yml` - Test profile
- `application-prod.yml` - Production profile

## Database Schema

The application uses PostgreSQL with the following main tables:

- `guests` - Guest information
- `rooms` - Room inventory
- `room_amenities` - Room amenities (many-to-many)
- `beds` - Individual beds within rooms
- `reservations` - Booking information
- `reservation_guests` - Guests associated with reservations

## Contributing

This is an internal management system. Follow Clean Architecture principles:

1. Keep domain layer pure (no framework dependencies)
2. Use cases should be in application layer
3. Infrastructure implements domain interfaces
4. API layer should be thin, delegating to use cases

## License

Internal project for DABO ERP system.

## Contact

For questions or issues, contact the development team.
