---
name: flyway-decoupled
description: Use when changing schema migrations, Flyway container wiring, or startup ordering for the template's decoupled migration flow.
---

# Flyway Decoupled

Use when a schema or startup change must preserve this repository's pattern of running Flyway outside the Spring Boot process.

## Destination

- `resources/flyway/db/migration/*.sql`
- `resources/flyway/pom.xml`
- `resources/flyway/run-migration.sh`
- `Dockerfile`
- `.docker-compose-local/application.yaml`
- `.docker-compose-local/infrastructure.yaml`
- `README.md` only when developer-facing execution guidance changes

## Use For

- adding new versioned SQL migrations
- changing Flyway runner commands, locations, or connection variables
- adjusting startup ordering between the database, migration step, and application
- keeping local and deployment-like migration flows reproducible

## Inputs

1. Schema change or migration behavior to implement
2. Expected database URL, credentials, and migration location
3. Whether the change affects local startup, deployment startup, or both
4. Compatibility, rollback, or rollout expectations

## Workflow

1. Add or update versioned SQL files under `resources/flyway/db/migration/` using Flyway naming.
2. Keep migration execution decoupled from the Spring Boot startup path; prefer the dedicated runner script or migration service over in-app hooks.
3. Align `resources/flyway/pom.xml`, `run-migration.sh`, and compose environment variables when connection settings or migration locations change.
4. Preserve explicit startup ordering so the application waits for the migration step in `.docker-compose-local/application.yaml`.
5. Keep `.docker-compose-local/infrastructure.yaml` and the application startup flow coherent when the local stack also needs Flyway changes.
6. If the work changes operational recovery or rollout procedure, route to `docs-runbook`; if it changes long-lived migration strategy, route to `docs-design-doc` or `docs-adr`.

## Output Shape

1. Versioned migration files
2. Minimal wiring changes for Dockerfile, compose, or Flyway runner files
3. README or runbook follow-up only when operator behavior changed

## Rules

- Do not move schema migration responsibility back into the application startup unless the architecture intentionally changes.
- Keep migrations append-only and versioned; prefer new scripts over rewriting applied ones.
- Keep connection settings and migration locations aligned across compose files and runner scripts.
- Use `make run-all` as the default validation target when startup wiring changes.
