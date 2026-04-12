# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

This is a **Java Architecture Template** following **Hexagonal Architecture** principles, designed to serve as a production-ready template for Spring Boot applications. The project demonstrates a user registration system with event-driven architecture, including address enrichment via external APIs and comprehensive observability.

## Architecture

### Hexagonal Architecture Structure
The codebase follows strict Hexagonal Architecture principles with layer separation enforced by ArchUnit tests:

- **Core Layer** (`br.com.helpdev.sample.core`):
  - `domain/`: Business entities, value objects, and domain exceptions
  - `ports/input/`: Interfaces for inbound interactions (commands, queries)
  - `ports/output/`: Interfaces for outbound interactions (persistence, external APIs)
  - `usecases/`: Application business workflows that orchestrate domain logic

- **Adapters Layer** (`br.com.helpdev.sample.adapters`):
  - `input/`: REST controllers, Kafka event listeners, entry points
  - `output/`: Repository implementations, external API clients, database adapters

- **Config Layer** (`br.com.helpdev.sample.config`): 
  - Spring configuration classes and dependency injection setup

### Key Architectural Rules
- Core layer is framework-agnostic and only accessed by Config and Adapters layers
- All ports must be interfaces ending with "Port"
- Use cases implement input ports and should be package-private
- Use cases cannot depend on other use cases (use domain services instead)
- Implementation classes should be package-private to hide implementation details

## Development Commands

### Building & Testing
```bash
# Run unit tests with coverage report
make run-unit-tests

# Run acceptance tests (Docker-based integration tests)
make run-acceptance-tests

# Run all tests (unit + integration)
make run-all-tests
```

### Running the Application
```bash
# Run application with local profile
make mvn-run

# Start infrastructure services (MySQL, Kafka, Flyway migration)
make run-infrastructure

# Start observability stack (Grafana, Prometheus, Jaeger, OpenTelemetry)
make run-observability

# Start both infrastructure and observability
make run-stack

# Start application services via Docker
make run-app

# Start complete stack (infrastructure + observability + application)
make run-all
```

### Stopping Services
```bash
# Stop infrastructure services
make stop-infra

# Stop observability services  
make stop-observability

# Stop application services
make stop-app

# Stop all services
make stop-all
```

### Maven Commands
```bash
# Use embedded Maven wrapper
./mvnw clean compile

# Run specific test class
./mvnw test -Dtest=ArchitectureTest

# Run tests for acceptance-test module only
./mvnw clean verify -pl acceptance-test

# Generate JaCoCo coverage report
./mvnw clean test jacoco:report
```

## Testing Strategy

### Unit Tests
- Located in `application/src/test/java`
- **ArchUnit tests** validate architectural compliance at `ArchitectureTest.java`
- JaCoCo enforces coverage requirements (configured in root `pom.xml`)
- Parallel test execution enabled for performance

### Acceptance Tests  
- Separate `acceptance-test` module with Docker-based integration testing
- Uses TestContainers for dynamic dependency management
- WireMock for external service mocking
- RestAssured for API testing
- Tests run against actual Docker image in real-world-like environment

### Architecture Validation
The `ArchitectureTest.java` class contains critical tests that enforce:
- Layer dependency rules
- Port naming conventions (must end with "Port")
- Use case structure and visibility
- Prevention of circular dependencies between use cases

## Key Technologies

### Core Stack
- **Java 21** with Spring Boot 3.4.1
- **MySQL 8.0** for persistence
- **Apache Kafka** for event streaming
- **Spring Cloud** with OpenFeign for external API calls
- **Resilience4j** for circuit breakers

### Observability
- **OpenTelemetry** for distributed tracing
- **Micrometer** for metrics collection
- **Jaeger** for trace visualization
- **Prometheus** for metrics storage
- **Grafana** for dashboards and monitoring

### Documentation
- **OpenAPI/Swagger** for REST API documentation (`http://localhost:8080/swagger-ui.html`)
- **AsyncAPI/Springwolf** for event documentation (`http://localhost:8080/springwolf/asyncapi-ui.html`)

## Database Management

### Flyway Migrations
- **Decoupled Flyway setup** for better performance and Kubernetes compatibility
- Migration scripts in `resources/flyway/db/migration`
- Flyway runs as separate Docker service before application startup
- Prevents race conditions and startup bottlenecks

### Connection Details
- Local MySQL: `localhost:3306/sampledb`
- Credentials: `user/password` (configurable via environment variables)

## Event Architecture

### Kafka Integration
- Bootstrap servers: `localhost:9092`
- Consumer group: `java-architecture-template@user-events`
- Producer/consumer configuration in `application.properties`
- Event listeners in `adapters/input/kafka/`
- Event dispatchers via output ports

### Sample Flow
1. User creation via REST API triggers business logic
2. `UserCreatorUseCase` orchestrates user creation and validation
3. Events dispatched to Kafka upon successful creation
4. `UserEventListener` consumes events for address enrichment
5. External API integration via OpenFeign for address data

## Configuration Management

### Profiles
- **local**: Development profile with local infrastructure
- Environment variables override default values
- Configuration centralized in `application.properties`

### External Integrations
- Random Data API for address enrichment: `https://random-data-api.com`
- Configurable via `RANDOM_DATA_API_URL`

## Development Guidelines

### Adding New Features
1. Start with domain entities in `core/domain/`
2. Define ports in `core/ports/input` and `core/ports/output`
3. Implement use cases in `core/usecases/`
4. Create adapters in `adapters/input` or `adapters/output`
5. Wire dependencies in `config/`
6. Run `ArchitectureTest` to ensure compliance

### Code Quality
- JaCoCo coverage requirements enforced in build
- Architecture tests prevent violations of hexagonal principles
- Use record classes for immutable domain entities
- Prefer package-private visibility for implementations

### Testing New Code
- Unit tests for core logic and use cases
- Integration tests in acceptance-test module for end-to-end flows
- Mock external dependencies with WireMock
- Use TestContainers for real infrastructure dependencies

## Architectural Decision Records

The project maintains ADRs in the `docs/adr/` directory documenting key architectural decisions:
- REST API adoption
- MySQL database choice  
- Kafka for event streaming

Reference `docs/adr/README.md` for the complete list and process for adding new ADRs.

## Local Development URLs

After running `make run-all`:
- **Application**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **AsyncAPI UI**: `http://localhost:8080/springwolf/asyncapi-ui.html`
- **Grafana**: `http://localhost:3000`
- **Prometheus**: `http://localhost:9090`
- **Jaeger**: `http://localhost:16686`

## Project Structure Reference

```
application/
├── src/main/java/br/com/helpdev/sample/
│   ├── core/
│   │   ├── domain/          # Entities, VOs, domain exceptions
│   │   ├── ports/
│   │   │   ├── input/       # Inbound port interfaces  
│   │   │   └── output/      # Outbound port interfaces
│   │   └── usecases/        # Application workflows
│   ├── adapters/
│   │   ├── input/           # REST controllers, Kafka listeners
│   │   └── output/          # JPA repositories, API clients
│   └── config/              # Spring configuration
acceptance-test/             # Docker-based integration tests
docs/
└── adr/                    # Architectural Decision Records
```
