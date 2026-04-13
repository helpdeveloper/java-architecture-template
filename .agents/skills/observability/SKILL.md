---
name: observability
description: Use when changing the local OpenTelemetry, Prometheus, Grafana, or Jaeger stack and its wiring to the application.
---

# Observability

Use when telemetry collection, export, or local observability services change and the template's metrics and traces setup must stay coherent.

## Destination

- `.docker-compose-local/observability.yaml`
- `.docker-compose-local/config/otel-collector.yaml`
- `.docker-compose-local/config/prometheus.yaml`
- `.docker-compose-local/config/ds-prometheus.yaml`
- `.docker-compose-local/application.yaml`
- `README.md` only when access URLs or startup guidance changes

## Use For

- changing OTLP endpoints or application telemetry environment variables
- adjusting OpenTelemetry Collector pipelines, exporters, or processors
- changing Prometheus scraping or remote-write configuration
- updating Grafana datasource wiring or Jaeger exposure
- troubleshooting missing local metrics, traces, or dashboards

## Inputs

1. The affected signal or user-visible behavior: metrics, traces, dashboards, or collector flow
2. The config or service being changed
3. Expected URLs, ports, networks, and dependencies
4. Any application environment changes required for the new telemetry path

## Workflow

1. Start from `.docker-compose-local/application.yaml` and confirm how the app points to OTLP endpoints.
2. Follow the signal path through `.docker-compose-local/config/otel-collector.yaml`.
3. Keep downstream exporters and services aligned in `.docker-compose-local/observability.yaml`.
4. Preserve Grafana `:3000`, Prometheus `:9090`, Jaeger `:16686`, and OTLP `:4317/:4318` access paths unless a deliberate change is required.
5. Keep Prometheus and Grafana configuration in sync with the collector and service ports.
6. If the change affects operational response or support workflows, route to `docs-runbook`; if it changes cross-cutting platform design, route to `docs-design-doc`.

## Output Shape

1. Minimal stack or config changes across application wiring, collector, Prometheus, Grafana, or Jaeger
2. A clear telemetry path that remains coherent end to end
3. README or runbook updates only when developer-visible behavior changed

## Rules

- Keep the telemetry path coherent end to end: app -> OTEL collector -> Jaeger or Prometheus -> Grafana.
- Prefer config-as-code under `.docker-compose-local/` over manual container tweaks.
- Do not change developer-facing ports casually; update docs when you do.
- Use `make run-observability` as the default validation target, plus `make run-app` when application wiring changes.
