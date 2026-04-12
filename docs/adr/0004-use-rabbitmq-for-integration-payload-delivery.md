# 4. Use RabbitMQ for Integration Payload Delivery

## Context
The service already uses Kafka for internal event streaming, as recorded in ADR `0003-use-kafka-for-event-streaming.md`.

We now need a second asynchronous integration path for a specific downstream system that expects a completed user payload after `enrichUser(...)` succeeds. This requirement is not about replacing Kafka or introducing a second event-streaming platform. It is about delivering a targeted payload to a dedicated integration channel.

## Decision
We will keep Kafka as the platform for event streaming and introduce RabbitMQ as a separate outbound channel for integration payload delivery.

The RabbitMQ publication will happen only after a successful `enrichUser(...)` execution and after the existing Kafka `sendUserAddressUpdatedEvent(...)` call returns.

## Consequences
- Kafka remains the source for event-streaming workflows in this service.
- RabbitMQ is added only for targeted microservice communication where the consumer expects a completed user payload.
- The application now depends on two asynchronous transports, which increases configuration, testing, and operational complexity.
- Local and acceptance-test infrastructure must include RabbitMQ support.

## Alternatives Considered
- Keep Kafka as the only asynchronous transport and force the integration consumer to consume streaming events.
- Replace Kafka with RabbitMQ for all asynchronous communication.
- Expose the completed user data through synchronous REST-only integration.

## Rationale
The downstream integration requirement is payload-oriented and broker-specific, while the existing Kafka flow is still the correct choice for event streaming.

Using RabbitMQ only for this dedicated integration preserves ADR `0003` instead of diluting it. It also keeps the implementation explicit: Kafka continues to describe domain event flow, while RabbitMQ handles a direct integration contract.

## Date
2026-04-12

## Status
Accepted

## Links
- [ADR 0003 - Use Kafka for Event Streaming](0003-use-kafka-for-event-streaming.md)
- [Kafka](https://kafka.apache.org/)
- [RabbitMQ](https://www.rabbitmq.com/)
