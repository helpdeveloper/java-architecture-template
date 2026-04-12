---
name: docs-runbook
description: Use when a change affects operations, support, incident response, rollout, rollback, or recovery workflows.
---

# Runbook Context

Use when the change affects operation, support, incident response, rollout, rollback, or recovery workflows.

## Destination

- `docs/runbooks/<slug>.md`

## Use For

- production procedures
- support and troubleshooting flows
- deployment and rollback routines
- incident response and escalation guidance

## Output Shape

1. Purpose
2. Preconditions and required access
3. Triggers or symptoms
4. Step-by-step actions
5. Verification points
6. Rollback or escalation path

## Rules

- Write for action under pressure: concrete steps beat abstract explanation.
- Keep commands, checks, and handoff points easy to scan.
- Update the live runbook before creating a duplicate.
- If the workflow will not be reused, keep it transient instead of promoting it to durable documentation.
