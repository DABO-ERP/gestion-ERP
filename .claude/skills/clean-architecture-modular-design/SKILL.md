---
name: clean-architecture-modular-design
description: Use this skill when structuring the project, defining layers, or enforcing architectural boundaries.
---

When applying Clean Architecture:

1. Enforce strict layers:
   - domain
   - application
   - infrastructure
   - api

2. Dependency rules:
   - Domain depends on nothing
   - Application depends on domain
   - Infrastructure depends on application
   - API depends on application

3. Rules:
   - No Spring annotations in domain
   - No JPA entities leaking into API
   - Use interfaces (ports) at boundaries

4. Business logic:
   - Lives in use cases
   - Not in controllers
   - Not in repositories

Violations must be considered architectural bugs.
Always write tests to verify layer boundaries are respected.