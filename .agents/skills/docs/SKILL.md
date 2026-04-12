---
name: docs
description: Route documentation-related requests to the right artifact skill and decide whether durable documentation is needed.
---

# Documentation Context Router

Use first for documentation requests that should improve execution context instead of just producing more text.

## Goal

- decide whether the change needs durable documentation
- prefer updating an existing artifact before creating a new one
- route to the right artifact skill when documentation is justified
- make “no new document needed” an explicit valid outcome

## Routing Rules

- Use `docs/adr/` when the change modifies architecture, constraints, guardrails, or long-lived technical decisions.
- Use `spec/` when the work needs explicit scope, scenarios, constraints, and verifiable acceptance criteria.
- Use `design-doc/` when the change has non-trivial integration, migration, structural impact, or technical risk.
- Use `runbook/` when the change affects operations, support, incident handling, rollout, or recovery.
- Use `selective-persistence/` after planning or execution to decide what remains durable and what should stay transient.

## Sequence

1. Clarify scope through definition, spec, or plan mode first.
2. If architecture or technical design needs durable context, write and validate the **ADR** and/or **Design Doc** next.
3. Start implementation only after those artifacts are aligned and validated when they are required.

## Decision Heuristics

Ask these questions before writing anything new:

1. What context would be expensive to rediscover in a few months?
2. Is there already a live document that should be updated instead?
3. Is this artifact meant to guide execution now, preserve durable context later, or both?
4. Would creating a new document reduce ambiguity, or just add noise?

## RFC Guidance

- Treat existing RFCs as context inputs when they already frame scope, alternatives, or open decisions.
- Do not generate a new RFC by default. Use one only when the change genuinely needs formal debate and alignment.

## Output

Produce a short routing decision with:

- recommended artifact type
- whether to update existing documentation or create something new
- why the artifact is justified
- whether any context should remain transient

## Rules

- Bias toward lean, decision-useful documentation.
- When an **ADR** or **Design Doc** is required, treat it as a pre-implementation artifact: write and validate it after definition/plan mode and before coding.
- Do not create durable docs for local, reversible, low-impact changes.
- If no artifact is justified, say so explicitly.
