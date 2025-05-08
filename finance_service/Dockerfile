# Stage 1: Build with Gradle
FROM gradle:7.6.4-jdk17 AS builder

# Copy source code into the container
WORKDIR /app
COPY . .

# Run the build (excluding tests for speed; remove -x test to include them)
RUN gradle build -x test

# Stage 2: Run the application with a minimal JDK image
FROM gradle:7.6.4-jdk17

WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
