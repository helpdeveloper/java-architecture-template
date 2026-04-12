---
name: api-doc-auditor
description: Use when REST or Kafka contracts change and you need OpenAPI and AsyncAPI to stay aligned with source annotations and runtime docs.
---

# API Doc Auditor

Use when endpoints, DTOs, error contracts, event payloads, or topics change and you need to keep generated API documentation accurate.

## Destination

- source annotations and config under `application/src/main/java/...`
- `README.md` only when access URLs or user-facing documentation behavior changes

## Use For

- changes to `@RestController` endpoints, request or response DTOs, or `ProblemDetail` responses
- changes to Kafka publishers or listeners, topics, payloads, or retry behavior
- checking whether Swagger or Springwolf output still matches the code
- identifying missing or stale OpenAPI or AsyncAPI annotations

## Inputs

1. Changed controller, listener, publisher, DTO, or config files
2. Expected HTTP contract or event contract
3. Any new status codes, fields, channels, or payload semantics

## Workflow

1. Audit REST sources first: controller mappings, DTO fields, validation annotations, error handlers, and `OpenApiConfig`.
2. Audit async sources next: `@AsyncPublisher`, `@AsyncListener`, `@AsyncOperation`, topic names, and payload records or DTOs.
3. Prefer fixing the source-of-truth code and annotations instead of editing generated specs.
4. Keep access paths accurate: `/swagger-ui.html`, `/v3/api-docs`, `/springwolf/asyncapi-ui.html`, and `/springwolf/docs`.
5. Update durable docs only when the public interface or documented access pattern truly changed.

## Output Shape

1. Mismatch list between code and generated docs
2. Source changes needed to restore alignment
3. Any README or durable-documentation follow-up that is actually justified

## Rules

- Do not hand-edit generated OpenAPI or AsyncAPI output.
- Keep DTO naming, JSON property mappings, and payload shapes aligned with runtime serialization.
- Make public contract changes explicit; do not rely on defaults when annotations would clarify behavior.
- Prefer source fixes over documentation-only patches.
- Use the running application and generated docs only as verification; the source code remains authoritative.
