---
name: docs-adr
description: Use when a change alters architecture, constraints, guardrails, or long-lived technical decisions and may need an ADR.
---

# ADR Context

Use when a change alters architecture, technical constraints, guardrails, or decisions with long-lived consequences.

## Destination

- `docs/adr/NNNN-title-of-decision.md`

Follow:

- `docs/adr/README.md`
- `docs/adr/template.md`

## Timing

- Write the ADR after scope and impact are clear through definition, spec, or plan mode.
- Validate the ADR before implementation starts when the change depends on that decision.

## Use For

- architecture choices that affect future changes
- trade-offs that should not be rediscovered repeatedly
- decisions that constrain implementation patterns or system boundaries
- updates to existing architectural rules

## Output Shape

Use the existing ADR template:

1. Title
2. Context
3. Decision
4. Consequences
5. Alternatives Considered
6. Rationale
7. Date
8. Status
9. Links

## Rules

- Prefer updating an existing ADR or superseding one before adding a new record.
- Focus on why the decision was made, not on retelling the full implementation plan.
- Keep the document durable: record the context that matters after the current change is over.
- Do not defer the ADR until after coding when the implementation depends on the architectural decision.
- Update `docs/adr/README.md` when a new ADR is added.
- If the change is local and reversible, ADR is usually the wrong artifact.
