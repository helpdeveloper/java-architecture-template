# Retroactive Design Doc: Planning the Java Architecture Template

- **Status:** Accepted
- **Date:** 2025-01-05
- **Owners:** Repository maintainers
- **Related artifacts:** [Repository README](../../README.md), [ADR 0001 - Adopt REST for API Communication](../adr/0001-adopt-rest-for-api-communication.md), [ADR 0002 - Use MySQL as the Primary Database](../adr/0002-use-mysql.md), [ADR 0003 - Use Kafka for Event Streaming](../adr/0003-use-kafka-for-event-streaming.md), [Acceptance Test README](../../acceptance-test/README.md)

## 1. Summary

This document reconstructs the intended design for the repository as if it had been written before implementation. The goal is to turn the current codebase into a reusable planning reference for future adopters of the template, not just a sample project with implicit conventions.

The template combines a Spring Boot 3 and Java 21 application, a hexagonal internal structure, synchronous REST and asynchronous Kafka flows, isolated database migration assets, generated runtime contracts, observability defaults, and Docker-based black-box acceptance tests. Together, these pieces define the baseline shape of a maintainable service starter.

## 2. Problem statement

Starting a new backend service usually involves repeating the same architectural decisions: where business logic belongs, how adapters are isolated, how contracts are documented, how local infrastructure is reproduced, and how confidence is built beyond unit tests. Without a deliberate template, teams often get only a folder scaffold and must rediscover guardrails while shipping features.

This repository needs a single design document that explains the complete plan behind the template: what problems it solves, how its pieces fit together, what trade-offs it makes, and how adopters should evolve it. Existing ADRs already capture isolated technology decisions, but they do not explain the full template shape or the intended adoption path.

## 3. Goals

- Provide a reference implementation of hexagonal architecture on Spring Boot 3 and Java 21.
- Keep the core domain and use cases framework-agnostic through explicit input and output ports.
- Demonstrate both synchronous request-response and asynchronous event-driven integration in the same template.
- Make architecture rules visible and enforceable through ArchUnit tests and package-private implementations.
- Provide a reproducible local stack for application runtime, migrations, messaging, persistence, and observability.
- Generate REST and event contracts from source code through OpenAPI and AsyncAPI tooling.
- Validate the template from the outside with Docker-based acceptance tests instead of relying only on in-process integration tests.

## 4. Non-goals

- Provide a complete production-ready business system beyond a minimal sample domain.
- Solve authentication, authorization, tenancy, or deployment automation end to end.
- Be technology-neutral out of the box; this template starts with opinionated defaults and expects future services to replace parts intentionally.
- Replace ADRs as the place for individual long-lived decisions such as choosing REST, MySQL, or Kafka.

## 5. Current state

### 5.1 Repository layout

| Area | Purpose |
| --- | --- |
| `application/` | Spring Boot application module containing adapters, core, config, tests, and runtime resources. |
| `acceptance-test/` | Black-box acceptance module that builds the app image and validates behavior with Testcontainers, WireMock, and RestAssured. |
| `resources/flyway/` | Standalone Flyway assets used for decoupled database migration execution. |
| `.docker-compose-local/` | Local runtime stack for application, infrastructure, and observability services. |
| `docs/adr/` | ADRs for isolated architectural decisions. |

### 5.2 Sample behavior encoded by the template

The sample domain is intentionally small, but it exercises the full template:

1. A client creates a user through `POST /user`.
2. The application validates domain constraints, persists the user in MySQL, and publishes a `user-events` Kafka event.
3. A Kafka listener reacts to the creation event, calls an external random address API, persists the address, and publishes an update event.
4. A client can later query the user through `GET /user/{uuid}` and observe the eventual address enrichment.

This flow is valuable because it demonstrates core concerns that a real service starter must support: validation, persistence, messaging, retries, eventual consistency, contract documentation, and acceptance coverage.

### 5.3 Existing guardrails

- The root and module `pom.xml` files establish a multi-module Maven build with `application` and `acceptance-test`.
- `ArchitectureTest` encodes the main structural rules: ports must be interfaces ending with `Port`, use cases must implement input ports, and implementations should stay package-private.
- Runtime contracts are exposed through Springdoc OpenAPI and Springwolf AsyncAPI.
- Database migrations are packaged separately under `resources/flyway/` and can run before the application starts.
- Acceptance tests run the built application image with real MySQL and Kafka containers plus a mocked external HTTP service.

### 5.4 ADR baseline

The current template already formalizes three key decisions:

- [ADR 0001](../adr/0001-adopt-rest-for-api-communication.md): REST is the synchronous API style.
- [ADR 0002](../adr/0002-use-mysql.md): MySQL is the primary relational store.
- [ADR 0003](../adr/0003-use-kafka-for-event-streaming.md): Kafka is the event streaming backbone.

This design doc assembles those separate decisions into a single planning narrative.

## 6. Proposed design

### 6.1 Module decomposition

| Module | Responsibilities | Main technologies |
| --- | --- | --- |
| `application` | Hosts the runnable service, the domain core, adapters, configuration, and unit/architecture tests. | Spring Boot, Spring MVC, Spring Data JPA, Spring Kafka, OpenFeign, Springdoc, Springwolf, Micrometer OTLP |
| `acceptance-test` | Builds the application image and runs black-box acceptance scenarios against containerized dependencies. | Maven Failsafe, Testcontainers, RestAssured, WireMock |
| `resources/flyway` | Keeps migration execution separate from app startup and packaging concerns. | Flyway |

The application module is the product template. The acceptance-test module proves that the template works as a deployable artifact. The Flyway assets keep schema evolution explicit and runnable in isolation.

### 6.2 Internal application structure

| Package area | Responsibility |
| --- | --- |
| `adapters.input.rest` | REST controllers, DTOs, request validation, and HTTP error mapping. |
| `adapters.input.kafka` | Kafka listeners that translate events into input port calls. |
| `adapters.output.db` | Persistence adapters and JPA mappings for domain entities. |
| `adapters.output.feign` | HTTP client adapters for external service integration. |
| `adapters.output.kafka` | Event publication adapters for outbound domain events. |
| `core.domain` | Domain entities, value objects, and domain exceptions. |
| `core.ports.input` | Application entry points exposed to adapters. |
| `core.ports.output` | Interfaces required by the core to reach external systems. |
| `core.usecases` | Package-private application services that orchestrate domain workflows. |
| `config` | Spring configuration and bootstrap wiring when explicit wiring is needed. |

The design intent is that adapters depend on ports, and the core depends only on domain concepts plus port interfaces. Package-private implementations keep the public surface small and make internals harder to couple to accidentally.

### 6.3 Main flows

#### User creation flow

`UserController` receives `POST /user`, maps the request DTO to a domain `User`, and calls `UserCreatorPort`. `UserCreatorUseCase` enforces the age-of-majority rule, stores the user through `UserRepositoryPort`, then publishes a user-created event through `UserEventDispatcherPort`.

#### User enrichment flow

`UserEventListener` listens on the `user-events` topic. For creation events it calls `UserEnricherPort`, which loads the user, fetches an address through `AddressClientPort`, persists it through `AddressRepositoryPort`, and emits a user-updated event through `UserEventDispatcherPort`.

The enrichment flow is intentionally asynchronous. It demonstrates eventual consistency, decouples API latency from external HTTP latency, and gives the template a realistic event-driven example instead of a purely CRUD-oriented design.

#### User query flow

`UserController` receives `GET /user/{uuid}` and delegates to `UserGetterPort`. `UserGetterUseCase` retrieves the user from persistence or fails with a domain-specific not-found exception that is translated to an HTTP `404 ProblemDetail`.

### 6.4 Cross-cutting concerns

- **Runtime contracts:** REST endpoints are documented by Springdoc and asynchronous channels by Springwolf.
- **Error handling:** `ControllerErrorHandler` maps domain and validation failures to Spring `ProblemDetail`.
- **Observability:** Actuator health endpoints plus OTLP metrics and tracing defaults integrate the application with Prometheus, Jaeger, and Grafana through the local stack.
- **Migration control:** Flyway runs independently from the application process, reducing coupling between schema changes and runtime startup.
- **Retry behavior:** The external address client retries HTTP calls, and the Kafka listener uses a retryable topic, making transient integration failures visible in the sample.

## 7. Dependencies and interfaces

### 7.1 Core ports and primary implementations

| Port | Direction | Main implementation | Purpose |
| --- | --- | --- | --- |
| `UserCreatorPort` | Input | `UserCreatorUseCase` | Create users from incoming application requests. |
| `UserGetterPort` | Input | `UserGetterUseCase` | Retrieve users by UUID. |
| `UserEnricherPort` | Input | `UserEnricherUseCase` | Execute asynchronous enrichment after a creation event. |
| `UserRepositoryPort` | Output | `UserRepository` | Persist and query users in MySQL via JPA. |
| `AddressRepositoryPort` | Output | `AddressRepository` | Persist enriched addresses. |
| `AddressClientPort` | Output | `AddressClient` | Fetch address data from an external HTTP service. |
| `UserEventDispatcherPort` | Output | `UserEventDispatcher` | Publish domain events to Kafka. |

### 7.2 External interfaces and runtime surfaces

| Surface | Type | Purpose | Notes |
| --- | --- | --- | --- |
| `POST /user` | REST | Create a user and return a resource location. | Implemented in `UserController`; documented in OpenAPI. |
| `GET /user/{uuid}` | REST | Read user state, including eventual address enrichment. | Implemented in `UserController`; documented in OpenAPI. |
| `user-events` | Kafka topic | Carry user lifecycle events. | Published and consumed by the application; documented in AsyncAPI. |
| MySQL `sampledb` | Database | Persist users and addresses. | Chosen by ADR 0002; migrations managed through Flyway assets. |
| Random Data API | External HTTP dependency | Provide address data for enrichment. | Accessed through OpenFeign with retry. |
| `/actuator/health` | Runtime health | Expose readiness and liveness-oriented health data. | Used by local runtime and acceptance startup checks. |
| `/v3/api-docs` and `/swagger-ui.html` | Runtime documentation | Expose REST contract artifacts. | Generated from source annotations and Spring configuration. |
| `/springwolf/docs` and `/springwolf/asyncapi-ui.html` | Runtime documentation | Expose event contract artifacts. | Generated from Kafka listener and publisher annotations. |

## 8. Alternatives considered

- **Layered Spring application without explicit ports:** simpler at first glance, but easier to couple controllers, persistence, and framework concerns directly into business logic.
- **Synchronous enrichment during `POST /user`:** would return a fully enriched payload immediately, but would also make API latency and availability depend on the external address provider and would remove the template's asynchronous example.
- **Running migrations inside application startup:** simpler to wire locally, but weaker for deployment control and more prone to startup-time contention in orchestrated environments.
- **Keeping all integration tests inside the application module:** lower module count, but less representative of a real deployable artifact and easier to blur the line between unit and black-box tests.

## 9. Risks and mitigations

| Risk | Why it matters | Mitigation |
| --- | --- | --- |
| Architecture drift in future template forks | Teams can keep the package structure while bypassing the intended dependency rules. | Keep ArchUnit tests, package-private implementations, and this design doc aligned. |
| Hidden contract drift between code and consumers | Template adopters may change endpoints or events without updating external expectations. | Generate OpenAPI and AsyncAPI from source and keep those runtime artifacts discoverable. |
| Local-stack complexity for new adopters | The template includes several moving parts and can feel heavy for simple services. | Provide Make targets, Docker Compose stacks, and a small sample domain that demonstrates why each part exists. |
| Retry amplification in the enrichment path | The template combines Feign retries with Kafka retries, which can multiply external requests. | Keep retry behavior explicit in the design and acceptance tests; adopters should evaluate idempotency and backoff when changing the flow. |
| External dependency instability | The address enrichment path depends on a third-party HTTP endpoint. | Isolate it behind `AddressClientPort`, use retry semantics, and validate failure behavior in acceptance tests with WireMock. |
| Partial enforcement of adapter sublayer rules | The detailed ArchUnit layered test currently uses `adapter` package matchers while source packages are under `adapters`, so sublayer checks are weaker than intended. | Treat broader architecture tests as the current guardrail and review the detailed rule when strengthening template governance. |

## 10. Migration or rollout notes

No migration is required for the current repository because this document is retroactive. It becomes the baseline planning artifact for future template evolution.

For teams adopting the template:

1. Rename Maven coordinates, base packages, and service metadata first.
2. Replace the sample `User` domain while preserving the `core`, `ports`, `usecases`, and `adapters` separation.
3. Review the existing ADRs and either keep them or supersede them before replacing MySQL, Kafka, or REST.
4. Adapt migration scripts, external clients, and local stack definitions to the new service.
5. Extend acceptance tests before adding substantial new behavior so the black-box contract remains the main confidence layer.
6. Update OpenAPI, AsyncAPI, and observability metadata so generated runtime docs still reflect the service accurately.

## 11. Validation strategy

- **Unit and architecture tests:** keep fast feedback in `application/`, especially for domain rules, use cases, and architectural boundaries.
- **Acceptance tests:** use `acceptance-test/` to build the real app image and validate HTTP, Kafka, persistence, and external integration behavior against containers.
- **Runtime contract checks:** expose and inspect OpenAPI and AsyncAPI artifacts after startup.
- **Operational smoke checks:** verify the local stack through Actuator health plus observability tooling endpoints.
- **Common commands:** `make run-unit-tests`, `make run-acceptance-tests`, `make run-all-tests`, `make run-all`, and `make stop-all` are the baseline entry points for template users.

## 12. Open questions

- Should the detailed ArchUnit layer rules be tightened so the adapter sublayer package names are fully enforced?
- Should future template changes require a design doc whenever they affect more than one module or more than one runtime contract?
- When adopters replace the default technologies, should the repository provide migration checklists or artifact templates beyond ADRs and this design-doc template?

## 13. References

- [Repository README](../../README.md)
- [Acceptance Test README](../../acceptance-test/README.md)
- [ADR 0001 - Adopt REST for API Communication](../adr/0001-adopt-rest-for-api-communication.md)
- [ADR 0002 - Use MySQL as the Primary Database](../adr/0002-use-mysql.md)
- [ADR 0003 - Use Kafka for Event Streaming](../adr/0003-use-kafka-for-event-streaming.md)
- [PicPay - Design Docs: a importancia da documentacao tecnica no desenvolvimento de software](https://medium.com/inside-picpay/design-docs-a-import%C3%A2ncia-da-documenta%C3%A7%C3%A3o-t%C3%A9cnica-no-desenvolvimento-de-software-30f75686ab7e)
