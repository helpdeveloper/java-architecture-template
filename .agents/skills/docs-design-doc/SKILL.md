---
name: docs-design-doc
description: Use when the solution has non-trivial structure, integration, migration, or technical risk that deserves a design document.
---

# Design Doc Context

Use when the solution has non-trivial technical structure, integration, migration, or risk that deserves explicit design reasoning.

## Destination

- `docs/design/YYYY-MM-DD-<slug>.md`

## Timing

- Write the design doc after definition, spec, or plan mode clarifies the problem and scope.
- Validate the design doc before implementation starts when the work depends on the proposed structure, integration, or rollout approach.

## Use For

- system changes with multiple moving parts
- integrations across modules, services, or external systems
- migrations, rollouts, or compatibility concerns
- designs that need impact analysis before implementation

## Output Shape

1. Problem statement
2. Current state
3. Proposed design
4. Dependencies and interfaces
5. Risks and mitigations
6. Migration or rollout notes
7. Validation strategy

## Rules

- Connect the design to any relevant ADRs or specs.
- Emphasize structure, impact, and risk over narrative detail.
- Keep the design specific enough to guide implementation, but not so broad that it duplicates every execution note.
- Do not treat the design doc as a post-implementation write-up when the design must guide the implementation.
- If the work only needs acceptance criteria, prefer a spec instead.
