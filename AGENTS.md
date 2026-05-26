# AGENTS.md — How to Work Effectively in This Repo

---

## Read This First
- This repo is a Java 17/Spring Boot 3.5+ monolith, strictly following Clean Architecture.
    - _Do not_ bypass layer boundaries. See `ARCHITECTURE.md` for specifics.
    - Domain is _pure_ (no Lombok, no frameworks); all orchestration goes through Application layer, all IO/infrastructure is behind interfaces.
- Full architecture map: `ARCHITECTURE.md`. High-level system/feature map: `PROJECT_SUMMARY.md`.

## Local Dev & Setup
- Always run `./verify.sh` first (compilation, test, bootJar build, Docker detection). Fails on error.
- For a full dev env: `./dev-start.sh` handles dockerized PostgreSQL, applies SQL migrations, does a clean build, and runs the app with the dev profile.
- You can also run stepwise (if needed):
  1. `docker compose up -d postgres` (run DB)
  2. Migrate schema: `psql -U gestion_user -d gestion_erp < db/migrations/V001__initial_schema.sql`
  3. Seed data (dev only): `psql -U gestion_user -d gestion_erp < db/migrations/V002__seed_data.sql`
  4. Start the app: `./gradlew bootRun --args='--spring.profiles.active=dev'`
- Profile selection:
    - `dev-start.sh` and Docker both default to the `dev` Spring profile.
    - Explicit config in `application.yml` & `application-*.yml` — most settings come from ENV vars or Docker Compose for container runs.

## Build, Test, and CI Flow
- **ALWAYS build/test with Gradle wrapper, never plain gradle.**
    - Example: `./gradlew clean build` (compiles & runs all tests)
- **Single test class:** `./gradlew test --tests "<ClassName>"`
- **Cucumber (acceptance) tests:**
    - `./gradlew test --tests "com.daboerp.gestion.acceptance.AcceptanceTestRunner"`
    - Use tags: `./gradlew test -Dcucumber.filter.tags="@guest-management"`
- **CI workflow mirrors local:**
    - `build` (skips tests), then `test` (all unit/integration/acceptance), then `checkstyle`, then `jacoco` for coverage. See `.github/workflows/review-pr.yml` for exact steps and env.
    - Coverage/artifact paths: `build/reports/jacoco/`, `build/reports/checkstyle/`.
    - Linting: `./gradlew checkstyleMain checkstyleTest`
    - Security: `./gradlew dependencyCheckAnalyze`

## Database & Migrations
- DB schema: `db/migrations/V001__initial_schema.sql`. Update only with new versioned files — never alter applied migrations.
- Seeding: `db/migrations/V002__seed_data.sql` (for dev/test only).
- Dev DB runs in Docker by default. Internal ports: DB on 5433 (host), app on 8081 (host == container if docker compose).

## App Entrypoint & Deployment
- Main: `com.daboerp.gestion.GestionErpApplication`
- Docker build is multi-stage: see `Dockerfile` for exact behavior (non-root user, health checks, ports).
- App exposes health endpoints at `/actuator/health` (and others via Spring Boot Actuator).

## Testing Quirks
- Integration + acceptance tests use H2 in-memory DB, auto clean between scenarios (`application-test.yml`).
- Reports: check `target/cucumber-reports/` for acceptance tests, or corresponding `build/` directories for unit/integration.
- Reset DB between scenarios using SQL or `@Sql` annotation; don't assume persistent state across scenarios.

## Style & Extension Rules
- Domain must _not_ depend on Spring, Lombok, or any external libs.
- Add new features by:
    1. Domain: entities/value objects/factories
    2. Add interfaces if needed (repositories, etc)
    3. Application: use case with a command
    4. Infrastructure: implementation details
    5. Wire up API/Controller only at the end
- Always add/modify tests in all affected layers.

## Reference Docs (do not duplicate):
- `ARCHITECTURE.md`: canonical rules for layering, DDD terms, extension patterns
- `PROJECT_SUMMARY.md`: feature coverage, tech stack, status
- `CUCUMBER_README.md`: acceptance test structure/how-to

---

Only edit this file to add new _critical_ operational facts learned from scripts/config/docs. Most framework or language conventions are intentionally omitted.
