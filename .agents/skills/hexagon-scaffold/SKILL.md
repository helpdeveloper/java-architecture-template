---
name: hexagon-scaffold
description: Use when implementing a new feature slice in the template's hexagonal architecture across core, ports, adapters, and tests.
---

# Hexagon Scaffold

Use when you need to add a new business capability that should follow the repository's ports-and-adapters structure from the start.

## Destination

- `application/src/main/java/.../core/domain`
- `application/src/main/java/.../core/ports/input`
- `application/src/main/java/.../core/ports/output`
- `application/src/main/java/.../core/usecases`
- `application/src/main/java/.../adapters/input/*`
- `application/src/main/java/.../adapters/output/*`
- matching tests under `application/src/test/java/...`

## Use For

- new aggregates or domain concepts
- new commands or queries exposed via REST or messaging
- new outbound integrations such as database, HTTP, or event publishing
- feature slices that need the usual DTO, mapper, port, use case, and adapter wiring

## Inputs

1. Business capability and observable operations
2. Domain fields and invariants
3. Required inbound adapters such as REST or Kafka
4. Required outbound adapters such as JPA, Feign, or Kafka
5. Validation, error, and persistence expectations

## Workflow

1. Start with `tdd-loop`: pick the smallest failing test that describes the next behavior increment, preferably in the core.
2. Define domain records or value objects and the smallest input or output port set that expresses the use case.
3. Implement package-private use cases that implement input ports and depend only on output ports and domain types.
4. Add inbound adapters that translate transport concerns into the input-port API.
5. Add outbound adapters that implement output ports and keep framework details out of the core.
6. Add mapper, DTO, and entity layers only where boundaries require them.
7. Mirror existing patterns before inventing new ones: `UserCreatorUseCase`, `UserController`, `UserRepository`, `AddressClient`, and `UserEventDispatcher`.
8. Keep the red-green-refactor cycle tight: make the smallest production change for the current failing test, then let `archunit-guard` verify the structure.

## Output Shape

1. Domain model and exceptions or value objects as needed
2. Input and output ports with the `Port` suffix
3. Package-private use case implementations with the `UseCase` suffix
4. Adapter implementations, DTOs or entities, and mappers only where needed
5. Unit test coverage for core logic and adapter boundaries, written first through the TDD loop

## Rules

- Keep the core framework-agnostic.
- Use cases must not depend on other use cases directly.
- Prefer package-private implementations to hide internals.
- Reuse existing naming, packaging, and mapper patterns.
- Use `make run-unit-tests` as the default validation target.
- If the feature changes long-lived architecture constraints, route to `docs-adr` or `docs-design-doc` before scaffolding.
