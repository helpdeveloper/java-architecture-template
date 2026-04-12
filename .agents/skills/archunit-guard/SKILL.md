---
name: archunit-guard
description: Use when changing packages, ports, use cases, or adapters and you need to diagnose or preserve the repository's ArchUnit rules.
---

# ArchUnit Guard

Use when a change may violate the hexagonal rules enforced in `application/src/test/java/br/com/helpdev/sample/ArchitectureTest.java`.

## Destination

- `application/src/test/java/br/com/helpdev/sample/ArchitectureTest.java`
- any production files whose package, suffix, visibility, or dependency direction violates the current rules

## Use For

- adding new ports, use cases, adapters, or config classes
- refactoring packages or moving responsibilities across layers
- explaining why an ArchUnit failure happened and what the narrowest safe fix is
- extending architecture rules when the architecture intentionally changes

## Inputs

1. The classes or packages being added or changed
2. The failing ArchUnit message, if available
3. Whether the intent is to comply with the current architecture or to change the architecture itself

## Workflow

1. Map each changed class to the repo's layers: `core`, `adapters`, and `config`.
2. Preserve the current guardrails by default: ports are interfaces ending with `Port`, use cases end with `UseCase`, and implementations stay package-private.
3. Check dependency direction before editing tests: adapters may depend on ports and domain types, but use cases must not depend on other use cases or input ports.
4. Fix production structure first when the failure comes from code drifting away from the intended architecture.
5. Only update `ArchitectureTest.java` when the architecture itself is intentionally changing.
6. If the change affects long-lived architectural rules, route to `docs-adr` before normalizing the new rule in code.

## Output Shape

1. Precise diagnosis of the violated rule
2. The smallest compliant code or package change, or a focused ArchUnit update
3. Any new assertions required to protect the new pattern

## Rules

- Do not weaken rules just to make a change pass.
- Prefer aligning package names and visibility with the existing structure over adding exceptions.
- Keep the core framework-agnostic.
- Use `make run-unit-tests` as the default validation target.
- Treat ArchUnit as a guardrail, not a nuisance to bypass.
