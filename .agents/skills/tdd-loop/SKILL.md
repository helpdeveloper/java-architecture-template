---
name: tdd-loop
description: Use when implementing new behavior or fixing behavior through a test-first red-green-refactor loop.
---

# TDD Loop

Use before changing production code when the task adds or changes observable behavior.

## Destination

- matching tests under `application/src/test/java/...`
- matching production code under `application/src/main/java/...`
- `acceptance-test/src/test/java/...` when behavior must be proven across runtime boundaries

## Use For

- new domain rules, use cases, or adapters
- bug fixes with a reproducible failing behavior
- refactors that need characterization coverage before code movement
- controller, repository, or integration changes that should be driven by tests first

## Inputs

1. The smallest observable behavior to add or change
2. The narrowest level that can expose it: domain, use case, adapter, or acceptance
3. The expected success result and any critical failure path
4. Existing classes, ports, or adapters that the change should extend

## Workflow

1. Choose the smallest useful test seam first. Prefer domain or use case tests before controller, repository, or acceptance coverage.
2. Write one failing test that captures the next behavior increment. Reuse the repository's JUnit 5 and Mockito style from `UserCreatorUseCaseTest`, `UserControllerTest`, and `UserRepositoryTest`.
3. Run the narrowest target that proves the test is red.
4. Implement the smallest production change that makes that one test pass.
5. Run the focused test again, then widen to `make run-unit-tests` when the slice is coherent.
6. Refactor only with the suite green, keeping naming, package boundaries, and port directions aligned with the hexagonal rules.
7. When the behavior crosses HTTP, messaging, containers, or external integrations, add or extend acceptance coverage with `acceptance-scenario-scaffold` after the unit-level loop is stable.
8. When the change introduces a new feature slice, combine this skill with `hexagon-scaffold`; if ArchUnit fails, use `archunit-guard` to fix structure instead of weakening rules.

## Output Shape

1. A failing test added before the production change
2. The smallest production change needed to satisfy the new behavior
3. Refactored code with the tests still green
4. Broader regression coverage only when the behavior boundary justifies it

## Rules

- Do not start production code for new behavior without first expressing the next increment as a test.
- Keep the loop small: one behavior increment, one failing test, one passing change.
- Prefer unit tests as the default TDD seam; use acceptance tests only when user-visible behavior spans module or infrastructure boundaries.
- Match existing naming and tooling: JUnit 5, Mockito, and the repository's `make run-unit-tests` and `make run-acceptance-tests` targets.
- If the task is documentation-only, pure wiring with no behavior change, or an unavoidable external contract spike, say why TDD is being limited instead of silently skipping it.
