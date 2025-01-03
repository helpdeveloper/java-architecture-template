# Build stage
FROM openjdk:21-jdk-slim AS build

# Install specific Maven version (e.g., 3.x.x)
ARG MAVEN_VERSION=3.9.9
RUN apt-get update && apt-get install -y curl \
    && curl -kfsSL https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz \
    && tar -xzf maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn \
    && rm -rf maven.tar.gz

# Install and configure Flyway
ARG FLYWAY_VERSION=7.5.2
RUN mkdir /flyway \
    && curl https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/${FLYWAY_VERSION}/flyway-commandline-${FLYWAY_VERSION}.tar.gz -o /flyway/flyway-commandline-${FLYWAY_VERSION}.tar.gz \
    && tar -xzf /flyway/flyway-commandline-${FLYWAY_VERSION}.tar.gz --strip-components=1 -C /flyway \
    && rm /flyway/flyway-commandline-${FLYWAY_VERSION}.tar.gz

WORKDIR /build

# Copy the root POM file
COPY pom.xml .

# Copy all module directories to resolve dependencies
COPY ./acceptance-test ./acceptance-test
COPY ./application ./application

# Copy Migration files to flyway folder
COPY resources/flyway/db/migration /flyway/sql

# Run Maven package on the specific application module
WORKDIR /build/application
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:21-jdk-slim AS run
WORKDIR /app

# Copy the built application JAR from the build stage
COPY --from=build /build/application/target/*.jar application.jar
COPY --from=build /flyway /flyway

# Expose the application port
EXPOSE 8080

# Set default JVM options including memory settings
ENV JAVA_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "application.jar"]
