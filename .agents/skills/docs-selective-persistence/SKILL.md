---
name: docs-selective-persistence
description: Use after planning or execution to decide what should remain as durable documentation and what should stay transient.
---

# Selective Persistence

Use after planning or execution to decide what should remain as durable documentation and what should stay transient.

## Goal

- protect context that would be expensive to rediscover
- avoid turning every plan, analysis, or conversation into permanent documentation
- keep the repository coherent by updating the right artifact instead of adding noise

## Decision Test

Use this question as the default filter:

**If this context disappears in three months, will rediscovering it be costly or risky?**

## Output

Choose one of these outcomes:

- update existing durable artifact
- create a new durable artifact
- keep notes transient
- explicitly record that no new document is needed

## Rules

- Durable documentation should preserve decisions, constraints, interfaces, criteria, and operational knowledge.
- Temporary analyses, decompositions, and working notes should stay transient unless future rediscovery cost is high.
- Prefer explicit non-creation over silent omission.
- If the problem is stale documentation, update the right document instead of creating another one.
