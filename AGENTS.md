# AGENTS.md

This file is the canonical instruction entrypoint for AI coding agents in this repository.

For Claude, Codex, and Copilot compatibility:

- use this file as the source of truth for repository instructions
- use `.agents/skills/` as the shared library of reusable markdown skills
- prefer updating the shared skill files over duplicating tool-specific guidance

Repository-native skills live under `.agents/skills/` for compatible agent CLIs.

## Project Overview

This repository is a Java architecture template based on Hexagonal Architecture. It demonstrates:

- a Spring Boot application on Java 21
- a modular hexagonal structure under `application/`
- acceptance tests under `acceptance-test/`
- architectural decisions under `docs/adr/`
- OpenAPI and AsyncAPI documentation for runtime interfaces

## Architecture Rules

- Keep the core layer framework-agnostic.
- Input and output ports live under `core/ports/` and should end with `Port`.
- Use cases implement input ports and should stay package-private.
- Use cases must not depend on other use cases directly.
- Prefer package-private implementations to hide internals.

## Commands

Use the existing Makefile targets:

- `make run-unit-tests`
- `make run-acceptance-tests`
- `make run-all-tests`
- `make mvn-run`
- `make run-all`
- `make stop-all`

## Documentation Skills

For documentation-related work, start with:

- `.agents/skills/docs/SKILL.md`

Then route to the relevant artifact skill:

- `.agents/skills/docs-adr/SKILL.md`
- `.agents/skills/docs-spec/SKILL.md`
- `.agents/skills/docs-design-doc/SKILL.md`
- `.agents/skills/docs-runbook/SKILL.md`
- `.agents/skills/docs-selective-persistence/SKILL.md`

## Implementation Skills

Use these repository-native skills for common delivery workflows. For new production behavior, start with `.agents/skills/tdd-loop/SKILL.md` and then layer on the more specialized skills below as needed.

- `.agents/skills/tdd-loop/SKILL.md` for driving new code implementation through a red-green-refactor loop
- `.agents/skills/hexagon-scaffold/SKILL.md` for adding a new feature slice across core, ports, adapters, and tests
- `.agents/skills/archunit-guard/SKILL.md` for diagnosing or preserving architecture rules enforced by ArchUnit
- `.agents/skills/acceptance-scenario-scaffold/SKILL.md` for black-box acceptance coverage in the `acceptance-test/` module
- `.agents/skills/api-doc-auditor/SKILL.md` for keeping OpenAPI and AsyncAPI output aligned with source code

## Documentation Conventions

- ADRs belong in `docs/adr/` and must follow the process in `docs/adr/README.md`.
- New ADRs should use `docs/adr/template.md` and the existing `NNNN-title-of-decision.md` naming scheme.
- Specs, design docs, and runbooks should live in `docs/specs/`, `docs/design/`, and `docs/runbooks/` respectively when those artifacts are created.
- Prefer updating an existing live document before creating a new one.
- Treat “no new document needed” as an explicit valid decision.

## Timing Rule for ADR and Design Doc

When an **ADR** or **Design Doc** is required:

1. clarify scope first through definition, spec, or plan mode
2. write and validate the artifact next
3. start implementation only after the artifact is aligned when the work depends on it
