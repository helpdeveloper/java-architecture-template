# 3. Use Kafka for Event Streaming

## Context
For this project, we need to decide on the event streaming platform.We have two options: Kafka and RabbitMQ.

## Decision
We will use Kafka as the event streaming platform for our project.

## Consequences
- Kafka is a distributed event streaming platform that is highly scalable and fault-tolerant.
- It provides high throughput and low latency for real-time data processing.
- Not as easy to set up and configure as RabbitMQ.
- Requires more resources and operational expertise.

## Alternatives Considered
- RabbitMQ: An open-source message broker that implements the Advanced Message Queuing Protocol (AMQP).

## Rationale
We chose Kafka because the order of events is critical for our project, and Kafka provides strong ordering guarantees. 

## Date
2024-12-31

## Status
Accepted

## Links
- [Kafka](https://kafka.apache.org/)
- [RabbitMQ](https://www.rabbitmq.com/)