---
name: spring-boot-mastery
description: Use this skill when configuring, structuring, or integrating Spring Boot components using native Spring Boot mechanisms without reimplementing existing functionality.
---

When applying Spring Boot Mastery:

1. Use **Spring Boot 3.2.x** with:
   - Auto-configuration enabled
   - Convention over configuration

2. Use official starters only:
   - spring-boot-starter-web
   - spring-boot-starter-security
   - spring-boot-starter-data-jpa
   - spring-boot-starter-validation
   - spring-boot-starter-actuator

3. Configuration rules:
   - All configuration via `application.yml`
   - Environment overrides via Docker env vars
   - No hardcoded configuration values

4. Bean management:
   - Prefer constructor injection
   - Avoid field injection
   - Keep configuration classes minimal

5. Profiles:
   - `dev`, `test`, `prod` profiles must exist
   - No profile-specific logic in code

Never bypass Spring Boot facilities with manual wiring unless absolutely required.
