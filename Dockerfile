# Build stage
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /build

# Copy the root POM file
COPY pom.xml .

# Copy all module directories to resolve dependencies
COPY ./acceptance-test ./acceptance-test
COPY ./application ./application

# Run Maven package on the specific application module
WORKDIR /build/application
RUN mvn clean package -DskipTests

# Run stage
FROM maven:3.9.11-eclipse-temurin-21 AS run
WORKDIR /app

# Copy the built application JAR from the build stage
COPY --from=build /build/application/target/*.jar application.jar

# Copy Migration files to flyway folder
COPY resources/flyway /flyway
RUN chmod +x /flyway/run-migration.sh

# Expose the application port
EXPOSE 8080

# Set default JVM options including memory settings
ENV JAVA_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "application.jar"]
