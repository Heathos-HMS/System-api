
# Build stage

FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy built jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]