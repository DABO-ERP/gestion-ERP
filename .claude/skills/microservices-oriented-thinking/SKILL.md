---
name: microservices-oriented-thinking
description: Use this skill when designing APIs, data ownership, or service boundaries in a microservices system.
---

When thinking in microservices:

1. The service must be:
   - Stateless
   - Independently deployable
   - Independently testable

2. Rules:
   - Database per service (PostgreSQL)
   - No shared schemas
   - No direct DB access across services

3. Auth service responsibilities:
   - Identity
   - Authentication
   - Authorization
   - Token issuance only

4. Communication:
   - Synchronous REST only
   - No async messaging

Never optimize for future scale at the cost of present clarity.
Always define clear service boundaries based on business capabilities.