# Design Docs

This folder contains design documents for changes or capabilities that need more context than a small ADR, issue, or pull request description. In this repository, a design doc complements ADRs by capturing the full shape of a solution: the problem, scope, structure, interfaces, risks, rollout guidance, and validation strategy.

## When to create a design doc

Create a design doc when the work has non-trivial structure, integration, migration, or technical risk. Typical examples:

- a new feature slice that changes multiple adapters, ports, or use cases
- a migration that affects runtime contracts, persistence, or infrastructure
- a cross-cutting concern such as observability, security, or deployment shape
- a template or platform change that other teams will copy and extend

Use a different artifact when it fits better:

- use an **ADR** for a single long-lived architectural decision
- use a **spec** for scope, scenarios, and acceptance criteria
- use a **runbook** for operational procedures

## Process

1. Clarify the problem and scope first.
2. Copy [template.md](template.md).
3. Link any relevant ADRs, specs, or runbooks instead of duplicating them.
4. Keep the document specific enough to guide implementation, but lean enough to stay maintained.
5. Add the new document to the index below.

## Naming convention

Design docs in this repository follow this pattern:

`YYYY-MM-DD-<slug>.md`

## Design doc index

| Date | Title | Status | Notes |
| --- | --- | --- | --- |
| 2025-01-05 | [Planning the Java Architecture Template](2025-01-05-planning-the-java-architecture-template.md) | Accepted | Retroactive baseline design doc for the current template. |

## Template

Use [template.md](template.md) to create new design docs.

## References

- [PicPay - Design Docs: a importancia da documentacao tecnica no desenvolvimento de software](https://medium.com/inside-picpay/design-docs-a-import%C3%A2ncia-da-documenta%C3%A7%C3%A3o-t%C3%A9cnica-no-desenvolvimento-de-software-30f75686ab7e)
- [ADR README](../adr/README.md)
