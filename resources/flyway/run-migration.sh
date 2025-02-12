#!/usr/bin/env bash

mvn flyway:migrate -f /flyway/pom.xml -Dflyway.url=${DATABASE_URL} -Dflyway.user=${DATABASE_USER} -Dflyway.password=${DATABASE_PASSWORD} -Dflyway.locations=filesystem:/flyway/db/migration