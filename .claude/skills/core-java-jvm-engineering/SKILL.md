---
name: core-java-jvm-engineering
description: Use this skill when implementing or reviewing core Java logic, domain models, performance-sensitive code, or JVM-related concerns in a Spring Boot microservice.
---

When applying Core Java & JVM Engineering skills, follow these rules strictly:

1. Use **Java 17 (LTS)** language features:
   - Records for immutable DTOs
   - Sealed classes where domain constraints apply
   - `Optional` only for return types, never fields
   - Streams only when they improve clarity

2. Enforce immutability by default:
   - Domain entities must expose behavior, not setters
   - Use constructors for mandatory invariants

3. Handle exceptions deliberately:
   - Never catch `Exception` or `RuntimeException` generically
   - Use domain-specific exceptions
   - Avoid exception-driven control flow

4. Be JVM-aware:
   - Avoid unnecessary object creation in hot paths
   - Prefer composition over inheritance
   - Avoid reflection unless required by Spring

5. Write code that is:
   - Readable without comments
   - Deterministic
   - Testable in isolation

Never introduce cleverness at the cost of clarity.