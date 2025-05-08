# Build stage using Gradle 9.x with JDK 21
FROM gradle:jdk21 AS builder
WORKDIR /home/gradle/project
COPY . /home/gradle/project
RUN gradle bootJar --no-daemon

# Run stage using Eclipse Temurin JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar ./app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]