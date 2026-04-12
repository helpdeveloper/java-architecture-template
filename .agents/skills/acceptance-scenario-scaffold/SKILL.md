---
name: acceptance-scenario-scaffold
description: Use when adding or extending Docker-based black-box acceptance tests in the acceptance-test module.
---

# Acceptance Scenario Scaffold

Use when a change needs end-to-end confidence through the `acceptance-test/` module instead of, or in addition to, unit tests.

## Destination

- `acceptance-test/src/test/java/...`
- `acceptance-test/src/test/java/br/com/helpdev/acceptance/mock/`
- `acceptance-test/src/test/resources/__files/`

## Use For

- new REST scenarios against the built application container
- asynchronous flows that cross REST, Kafka, database, and external HTTP integrations
- actuator and health endpoint smoke checks
- failure and retry scenarios that depend on Testcontainers or WireMock

## Inputs

1. Scenario name and observable outcome
2. Entry point such as an HTTP request, startup behavior, or async side effect
3. External integrations to stub or observe
4. Success and failure assertions
5. Whether the scenario is synchronous or eventually consistent

## Workflow

1. Reuse `DefaultContainerStarter` unless the scenario truly needs a different container topology.
2. Keep the test black-box: drive behavior through HTTP, container startup, or externally visible side effects.
3. Use RestAssured assertions for HTTP contracts and follow the request style already used in `UserEndpointsIT`.
4. Reuse or extend mock helpers like `RandomDataApiMock` and add payload files under `__files/` when fixture bodies are needed.
5. For async flows, follow the `waitAtMost(...).untilAsserted(...)` pattern used in `UserEnrichProcessIT`.
6. Assert outcomes that users or integrators can observe: status codes, headers, follow-up reads, and WireMock invocation counts.
7. Keep shared-container tests deterministic, and opt into same-thread execution when timing or shared state requires it.

## Output Shape

1. Acceptance test class extending `DefaultContainerStarter`
2. Any required mock helper updates
3. Mock payload files, if the scenario needs fixture bodies
4. Minimal happy-path and critical failure-path assertions

## Rules

- Prefer one scenario per business behavior instead of mirroring every unit test here.
- Do not reach into Spring beans, repositories, or internal classes from acceptance tests.
- Prefer existing container wiring and helpers over bespoke setup.
- Use `make run-acceptance-tests` as the default validation target.
- If the change is only internal and fully covered by unit tests, skip acceptance coverage explicitly instead of forcing one.
