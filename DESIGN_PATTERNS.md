# Patrones de Diseño Aplicados - Gestion ERP

Este documento describe los patrones de diseño implementados para mejorar la escalabilidad, mantenibilidad y limpieza del código en el sistema de gestión hotelera.

## Patrones Implementados

### 1. Builder Pattern
**Ubicación:** `Reservation.ReservationBuilder`, `GuestFactory.GuestBuilder`  
**Propósito:** Construcción fluida y flexible de objetos complejos

```java
Reservation reservation = Reservation.builder()
    .checkIn(LocalDate.of(2024, 6, 15))
    .checkOut(LocalDate.of(2024, 6, 18))
    .guestPrincipal(guest)
    .room(room)
    .quotedAmount(new BigDecimal("300.00"))
    .source(Source.DIRECT)
    .addGuest(additionalGuest)
    .build();
```

**Beneficios:**
- Construcción paso a paso de objetos complejos
- Validación centralizada antes de la creación
- Flexibilidad en el orden de los parámetros
- Código más legible y mantenible

### 2. Domain Events Pattern
**Ubicación:** `domain/event/`, `GuestCreatedEvent`, `ReservationCreatedEvent`  
**Propósito:** Comunicación desacoplada entre agregados

```java
// Evento se genera automáticamente cuando se crea un guest
Guest guest = Guest.create(firstName, lastName, email, ...);
// Event: GuestCreatedEvent se publica automáticamente

// Los handlers reaccionan al evento sin acoplamiento
@EventListener
public void handleGuestCreated(GuestCreatedEvent event) {
    // Procesar workflows: email bienvenida, analytics, etc.
}
```

**Beneficios:**
- Desacopla la lógica de negocio principal de efectos secundarios
- Facilita la extensibilidad sin modificar código existente
- Mejora la trazabilidad de cambios en el dominio
- Permite procesamiento asíncrono de operaciones no críticas

### 3. Strategy Pattern  
**Ubicación:** `domain/strategy/pricing/`  
**Propósito:** Algoritmos de pricing intercambiables

```java
// Configuración de estrategias
PricingContext pricingContext = new PricingContext(List.of(
    new PeakSeasonPricingStrategy(),    // Prioridad 10
    new LongStayPricingStrategy(),      // Prioridad 5  
    new StandardPricingStrategy()       // Prioridad 1 (fallback)
));

// Cálculo automático usando la estrategia más apropiada
BigDecimal price = pricingContext.calculatePrice(reservation);
```

**Estrategias implementadas:**
- **StandardPricingStrategy:** Precio base por noche
- **LongStayPricingStrategy:** 10% descuento para estadías 7+ noches  
- **PeakSeasonPricingStrategy:** 25% incremento en temporada alta

**Beneficios:**
- Extensibilidad: nuevas estrategias sin modificar código existente
- Separación de responsabilidades en lógica de pricing
- Facilita testing de cada estrategia por separado
- Configuración flexible mediante prioridades

### 4. Specification Pattern
**Ubicación:** `domain/specification/`  
**Propósito:** Encapsulación de reglas de negocio complejas y consultas

```java
// Definición de especificaciones componibles
Specification<Reservation> activeLongStay = 
    new ActiveReservationSpecification()
        .and(LongStayReservationSpecification.standardLongStay());

// Uso para filtrado y validación
boolean matches = activeLongStay.isSatisfiedBy(reservation);
```

**Especificaciones implementadas:**
- **ActiveReservationSpecification:** Reservas en estado activo
- **DateRangeReservationSpecification:** Reservas en rango de fechas
- **LongStayReservationSpecification:** Estadías prolongadas

**Beneficios:**
- Reutilización de reglas de negocio complejas
- Composición flexible usando AND, OR, NOT
- Testing independiente de cada especificación
- Código más expresivo y legible

### 5. Command Handler Pattern (CQRS)
**Ubicación:** `application/command/`, `application/usecase/`  
**Propósito:** Separación clara entre comandos y consultas

```java
// Definición del comando
CreateGuestCommand command = CreateGuestCommand.create(
    firstName, lastName, email, phone, dateOfBirth, 
    nationality, documentNumber, documentType
);

// Ejecución via handler
Guest guest = createGuestUseCase.handle(command);
```

**Beneficios:**
- Separación clara de responsabilidades
- Comandos inmutables y auditables
- Facilita decoradores y middleware
- Mejor trazabilidad y logging

### 6. Factory Pattern Avanzado  
**Ubicación:** `domain/factory/guest/GuestFactory`  
**Propósito:** Creación consistente con validaciones de negocio

```java
GuestFactory factory = new GuestFactory();

Guest guest = factory.create(
    GuestFactory.builder()
        .firstName("John")
        .lastName("Doe")
        .email("john@example.com")
        .nationality(Nationality.US)
        .documentType(DocumentType.PASSPORT)
        .documentNumber("PASSPORT123")
);
```

**Validaciones incluidas:**
- Edad mínima (16 años)
- Formato de email válido
- Longitud de documento según tipo
- Campos obligatorios

**Beneficios:**
- Validaciones centralizadas y consistentes
- Separación de construcción y validación
- Reutilización en diferentes contextos
- Facilita testing de validaciones

### 7. Decorator Pattern
**Ubicación:** `application/decorator/`  
**Propósito:** Funcionalidades transversales sin modificar lógica central

```java
// Aplicación en configuracion Spring
@Bean
public CreateGuestUseCase createGuestUseCase(...) {
    CreateGuestUseCase baseUseCase = new CreateGuestUseCase(...);
    
    // Aplicar decoradores: Validation -> Logging
    var validationDecorator = new ValidationCommandHandlerDecorator<>(baseUseCase, validator);
    return new LoggingCommandHandlerDecorator<>(validationDecorator);
}
```

**Decoradores implementados:**
- **LoggingCommandHandlerDecorator:** Logging automático de ejecución
- **ValidationCommandHandlerDecorator:** Validación Bean Validation

**Beneficios:**
- Funcionalidades transversales sin contaminar lógica de negocio
- Composición flexible de comportamientos
- Principio de responsabilidad única
- Facilita testing unitario del core logic

## Arquitectura Resultante

### Beneficios Globales

1. **Escalabilidad:**
   - Nuevas estrategias de pricing sin modificar código existente
   - Eventos permiten agregar funcionalidades sin acoplamiento
   - Especificaciones reutilizables para nuevos casos de uso

2. **Mantenibilidad:**  
   - Separación clara de responsabilidades
   - Código más expresivo y legible
   - Testing independiente de cada componente
   - Validaciones centralizadas

3. **Flexibilidad:**
   - Composición dinámica de comportamientos
   - Configuración externa de estrategias
   - Extensibilidad sin modificar código existente

4. **Calidad del Código:**
   - Principios SOLID respetados
   - Bajo acoplamiento, alta cohesión  
   - Inmutabilidad donde es apropiado
   - Validaciones consistentes

### Clean Architecture Mejorada

```
┌─────────────────┐
│   API Layer     │  Controllers + DTOs
├─────────────────┤
│ Application     │  Use Cases + Commands + Decorators  
├─────────────────┤
│   Domain        │  Entities + Events + Strategies + Specifications
├─────────────────┤
│ Infrastructure  │  Persistence + Event Handlers + Configuration
└─────────────────┘
```

Los patrones implementados respetan completamente la Clean Architecture original mientras agregan:
- Mejor expresividad del dominio (Strategy, Specification)
- Desacoplamiento mejorado (Domain Events)
- Construcción consistente (Factory, Builder)
- Funcionalidades transversales limpias (Decorator)
- Comandos auditables (Command Handler)

## Testing

Cada patrón incluye tests unitarios completos que validan:
- **Factory Pattern:** Validaciones de negocio y construcción correcta
- **Strategy Pattern:** Selección correcta de algoritmos y cálculos
- **Specification Pattern:** Composición lógica y evaluación correcta

Esta implementación establece una base sólida para el crecimiento futuro del sistema manteniendo alta calidad y arquitectura limpia.