# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Run the application
./gradlew bootRun

# Build JAR (includes tests)
./gradlew build

# Build JAR skipping tests
./gradlew build -x test

# Run tests
./gradlew test
```

**Platform:** Java 17, Spring Boot 3.3.4, Gradle wrapper (`gradlew.bat` on Windows).

## Environment Setup

The application requires three environment variables for the database connection:

```
bdd=jdbc:postgresql://localhost:5432/websocial_dpa
pgUser=postgres
pgPassword=yourpassword
```

Activate the dev profile to include seed data: `--spring.profiles.active=dev`

## Project Structure

The codebase is organized into 19 feature packages under `src/main/java/com/infsis/socialpagebackend/`:

- **authentication/** — JWT login/register flow
- **posts/, comments/, replies/, reactions/** — Core social content
- **institutions/** — Multi-tenant institution management
- **followers/, sections/, navigation/** — Social graph and content organization
- **medias/** — File upload/storage (images, videos, documents)
- **moderation/** — Pluggable content moderation pipeline
- **multitenant/** — Tenant context resolution and isolation
- **social_networks/** — Meta/Facebook Graph API integration
- **security/** — JWT filters, Spring Security config, CORS
- **configuration/, constants/, enums/, exceptions/, validation/** — Cross-cutting concerns

Each feature package follows a consistent layer structure:
```
[module]/
├── controllers/     (@RestController, REST endpoints)
├── services/        (business logic)
├── repositories/    (Spring Data JPA)
├── models/          (JPA entities)
├── dtos/            (request/response objects)
└── mappers/         (DTO ↔ Entity conversion)
```

## Architecture

**Multi-tenancy:** Single database, shared schema with `institution_id` discriminator on all tenant-scoped tables. Users with `institution_id = NULL` are global ROOT users. Tenant context is resolved from the JWT token on each request via `TenantResolutionFilter`.

**Authentication:** JWT-based with access tokens (3h) and refresh tokens (72h). Invalidated tokens are stored in the `invalid_token` table. Tenant and user context are embedded in the JWT payload.

**API base path:** `/api/v1` (configurable via `app.api.base-path`). Auth endpoints are under `/api/auth/`.

**File storage:** Files are stored on disk under `${user.dir}/storage/`. 50MB per-file limit. MIME type validation is enforced.

**Content moderation pipeline** (`moderation/`): Three chained filters configurable per environment:
1. **Blacklist** — Always active, uses Aho-Corasick multi-pattern matching
2. **Perspective API** — Google toxicity detection (optional)
3. **LLM** — OpenAI-compatible endpoint, supports Ollama/DeepSeek/Groq (optional)

## Database Migrations (Flyway)

Migrations are in `src/main/resources/db/`:
- `migration/` — Versioned scripts (`V0__` through `V5__`), run in all environments
- `seed/` — Repeatable scripts (`R__dev_seed_dpa.sql`), dev-only (included via `application-dev.properties`)

Flyway runs automatically on startup. `ddl-auto=none` — schema is exclusively managed by Flyway.

**To add a new migration:** Create `V{N}__description.sql` in `db/migration/`. Flyway picks it up on next startup.

**Important:** Never modify existing versioned migration files. Flyway validates checksums. For fixes, create a new versioned script.

## Key Configuration

`application.properties` — base config (port 9091, JWT settings, moderation, logging)  
`application-dev.properties` — adds `db/seed/` location, enables dev seed data  
`application-prod.properties` — removes seed, disables SQL logging, sets prod URLs

HTTP request/response logging is handled by Logbook (level `TRACE`). Authorization headers and passwords are automatically masked.

Log output: `logs/websocial.log`
