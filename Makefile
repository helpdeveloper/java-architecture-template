# -----------------------------------------------------------------------------
# Project: java-architecture-template
# Description: Makefile for facilitating the execution of local development tasks.
# Author: Guilherme Zarelli
#
# Use: make <target>
# Example: make run-unit-tests
# -----------------------------------------------------------------------------

dockerRunArgs = -d --build

run-unit-tests:
	./mvnw clean test jacoco:report

run-acceptance-tests:
	./mvnw clean verify -pl acceptance-test

run-all-tests:
	./mvnw clean verify

mvn-run:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local -pl application

run-infrastructure:
	docker-compose -f .docker-compose-local/infrastructure.yaml up $(dockerRunArgs)

run-observability:
	docker-compose -f .docker-compose-local/observability.yaml up $(dockerRunArgs)

run-stack: run-infrastructure run-observability

run-app:
	docker-compose -f .docker-compose-local/application.yaml up $(dockerRunArgs)

run-all: run-stack run-app

stop-infra:
	docker-compose -f .docker-compose-local/infrastructure.yaml down

stop-observability:
	docker-compose -f .docker-compose-local/observability.yaml down

stop-stack: stop-infra stop-observability

stop-app:
	docker-compose -f .docker-compose-local/application.yaml down

stop-all: stop-stack stop-app