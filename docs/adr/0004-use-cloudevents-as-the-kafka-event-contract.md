# 4. Use CloudEvents as the Kafka Event Contract

## Context

The application already uses Kafka to publish domain events on the `user-events` topic, but the current payload is a custom JSON shape that mixes transport metadata and business data. That makes the contract ad hoc, limits interoperability with external consumers, and leaves event metadata such as source, type, identifier, and content type without a standard representation.

Issue #3 asks the project to implement the CloudEvents specification using the Java SDK, so the team needs a durable decision for how Kafka events should be represented from now on.

## Decision

We will publish and consume Kafka events as **CloudEvents v1.0 in structured JSON format**.

The event contract will follow these rules:

- use the Java CloudEvents SDK to build and parse events
- serialize messages as `application/cloudevents+json`
- use the CloudEvent `type` attribute to represent the domain event kind
- use the CloudEvent `data` attribute for the business payload
- keep the Kafka record key tied to the user identifier so ordering semantics stay unchanged for a given user

## Consequences

- Kafka events become self-describing and interoperable with CloudEvents-aware tooling and consumers.
- Event metadata is normalized through standard attributes instead of custom JSON conventions.
- The async contract becomes easier to document consistently in AsyncAPI because the envelope is explicit.
- Publishers and listeners must map between domain payloads and the CloudEvents envelope.
- Consumers of the existing custom JSON payload need to understand the new CloudEvents envelope.

## Alternatives Considered

- **Keep the current custom JSON payload**: simpler short term, but it keeps the contract proprietary and pushes metadata conventions into custom code.
- **Use CloudEvents binary mode over Kafka headers**: valid and efficient, but it spreads the contract across headers and payload, which makes the sample harder to inspect, test, and document than a self-contained structured JSON message.

## Rationale

Structured JSON CloudEvents gives the template a standards-based event contract without changing the existing topic topology or ordering behavior. It keeps each Kafka message self-contained, which fits the template's goals of clarity, learnability, and generated documentation while still aligning with the CloudEvents specification and SDK.

## Date

2026-04-12

## Status

Accepted

## Links

- [ADR 0003: Use Kafka for Event Streaming](0003-use-kafka-for-event-streaming.md)
- [Issue #3](https://github.com/helpdeveloper/java-architecture-template/issues/3)
- [CloudEvents Specification](https://cloudevents.io/)
- [CloudEvents Java SDK](https://github.com/cloudevents/sdk-java)
