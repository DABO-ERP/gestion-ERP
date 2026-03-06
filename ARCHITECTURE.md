# Gestion ERP - Architecture Documentation

## Overview

Gestion ERP is a production-ready hostel/hotel management microservice built following **Clean Architecture** principles, designed for internal business management operations.

## Design Principles

### 1. Clean Architecture (Hexagonal Architecture)

The application is structured in concentric layers with strict dependency rules:

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
│  (JPA, PostgreSQL, Mappers)             │
└─────────────────────────────────────────┘
```

**Key Rules:**
- Domain layer has NO external dependencies
- Application layer depends ONLY on domain
- Infrastructure implements domain interfaces
- API layer orchestrates use cases

### 2. Domain-Driven Design

**Aggregates:**
- `Guest` - Guest identity and information
- `Room` - Room inventory and configuration
- `Reservation` - Booking and stay management

**Value Objects:**
- `GuestId`, `RoomId`, `ReservationId` - Entity identifiers
- `Nationality`, `DocumentType`, `Source` - Enumerations
- `RoomStatus`, `Amenity`, `StatusType` - Domain concepts

**Repositories:**
- Abstract persistence concerns
- Domain interfaces, infrastructure implementations

### 3. SOLID Principles

**Single Responsibility:**
- Each use case handles one business operation
- Domain entities manage their own invariants

**Open/Closed:**
- Use cases closed for modification, open via composition
- New features can be added without changing existing code

**Liskov Substitution:**
- All repository implementations are substitutable

**Interface Segregation:**
- Repository interfaces are focused and minimal

**Dependency Inversion:**
- High-level use cases depend on abstractions

## Layer Details

### Domain Layer (`domain/`)

**Pure business logic with no framework dependencies.**

#### Entities
- **Guest**: Aggregate root managing guest identity, documents, and notes
  - Encapsulates business rules (email validation, contact updates)
  - No setters, only behavior methods
  - Immutable creation timestamp

- **Room**: Aggregate root managing room inventory and status
  - Manages room status transitions
  - Handles bed assignments
  - Validates room capacity

- **Reservation**: Aggregate root managing bookings
  - Handles reservation lifecycle (confirm, check-in, check-out, cancel)
  - Validates date ranges and capacity
  - Manages guest associations

#### Value Objects
- Enforce invariants at construction
- Immutable by design
- Self-validating

#### Repository Interfaces
- Define persistence contracts
- No implementation details
- Return domain entities

### Application Layer (`application/`)

**Business logic orchestration - framework agnostic.**

#### Use Cases
Each use case represents a single business operation:

**Guest Use Cases:**
- `CreateGuestUseCase` - Register new guest
- `GetGuestUseCase` - Retrieve guest information
- `ListGuestsUseCase` - Search and list guests
- `UpdateGuestUseCase` - Modify guest information

**Room Use Cases:**
- `CreateRoomUseCase` - Add new room
- `CreateRoomTypeUseCase` - Define room type
- `ListRoomsUseCase` - View room inventory
- `FindAvailableRoomsUseCase` - Find available rooms by criteria

**Reservation Use Cases:**
- `CreateReservationUseCase` - Create new booking
- `CheckInReservationUseCase` - Check-in guest
- `CheckOutReservationUseCase` - Check-out guest
- `ListReservationsUseCase` - Query reservations

#### Commands
- Immutable data carriers
- Input validation at construction
- Used by use cases

#### Exceptions
- `ResourceNotFoundException` - Entity not found
- `ResourceAlreadyExistsException` - Duplicate entity
- `BusinessRuleViolationException` - Domain rule violated

### Infrastructure Layer (`infrastructure/`)

**Technical implementation of domain contracts.**

#### Persistence
- **JPA Entities**: Framework-specific persistence model
- **JPA Repositories**: Spring Data JPA repositories
- **Mappers**: Convert between domain and JPA entities
- **Repository Implementations**: Bridge domain and persistence

#### Configuration
- **ApplicationConfig**: Dependency injection setup
- Wires use cases with repositories
- Pure Java configuration (no XML)

### API Layer (`api/`)

**REST API exposition - thin layer.**

#### Controllers
- Delegate to use cases
- Handle HTTP concerns
- Map DTOs to commands
- Return appropriate status codes

#### DTOs (Data Transfer Objects)
- Request/Response models
- Validation annotations
- No business logic

#### Exception Handling
- **GlobalExceptionHandler**: Centralized error handling
- Maps domain exceptions to HTTP responses
- Consistent error format

## Data Flow

### Creating a Reservation

```
1. Client → POST /api/v1/reservations (CreateReservationRequest)
2. ReservationController → validates DTO
3. ReservationController → creates CreateReservationCommand
4. CreateReservationUseCase → executes business logic:
   - Validates guest exists (GuestRepository)
   - Validates room exists and availability (RoomRepository)
   - Checks for overlapping reservations
   - Creates Reservation domain entity
   - Saves via ReservationRepository
5. ReservationRepository → maps to JPA entity
6. JPA Repository → persists to PostgreSQL
7. Domain entity → returned to controller
8. Controller → maps to ReservationResponse
9. Client ← 201 Created + ReservationResponse
```

## Design Patterns

### Repository Pattern
- Abstracts data access
- Domain defines interface
- Infrastructure provides implementation

### Factory Pattern
- Domain entities created via factory methods
- `Guest.create()`, `Room.create()`, etc.
- Enforces invariants at creation

### Command Pattern
- Use case commands encapsulate requests
- Immutable and validated
- Decouples API from application layer

### Mapper Pattern
- Converts between layers
- Isolates JPA entities from domain
- Prevents framework leakage

## Testing Strategy

### Unit Tests
- Domain entities and value objects
- Use cases with mocked repositories
- No framework dependencies

### Integration Tests
- Repository implementations
- Database interactions
- Spring context

### API Tests
- Controller endpoints
- Request/response validation
- Error handling

## Database Design

### Schema Principles
- Normalized to 3NF
- Foreign key constraints
- Check constraints for business rules
- Indexes on frequently queried columns

### Migration Strategy
- Versioned SQL scripts
- Forward-only migrations
- Never modify applied migrations

## Security Considerations

### Current Implementation
- Input validation via Bean Validation
- SQL injection prevention via JPA
- Error message sanitization

### Future Enhancements
- Integration with auth-ERP for authentication
- Role-based access control
- Audit logging

## Performance Considerations

### Database
- Connection pooling (HikariCP)
- Indexed foreign keys
- Efficient queries in JPA repositories

### Application
- Stateless design for horizontal scaling
- Eager/lazy loading optimization
- Caching strategy (future)

## Monitoring & Observability

### Spring Boot Actuator
- `/actuator/health` - Health checks
- `/actuator/info` - Application info
- `/actuator/metrics` - Metrics

### Logging
- Structured logging
- Log levels per profile
- No sensitive data in logs

## Deployment

### Docker
- Multi-stage build
- Non-root user
- Health checks
- Alpine-based for smaller image

### Docker Compose
- Development environment
- PostgreSQL included
- Network isolation
- Volume persistence

## Evolution & Maintenance

### Adding New Features
1. Start with domain (entities, value objects)
2. Define repository interface if needed
3. Create use case in application layer
4. Implement infrastructure if needed
5. Add API endpoint
6. Write tests at each layer

### Modifying Existing Features
1. Identify impacted layer(s)
2. Update domain if business logic changes
3. Update use cases if workflow changes
4. Update API if interface changes
5. Update tests

### Deprecating Features
1. Mark as deprecated in API
2. Provide migration path
3. Remove after grace period
4. Update documentation

## Related Projects

- **auth-ERP**: Authentication and authorization microservice
- Future microservices in DABO ERP ecosystem

## References

- Clean Architecture (Robert C. Martin)
- Domain-Driven Design (Eric Evans)
- Spring Boot Documentation
- PostgreSQL Documentation
