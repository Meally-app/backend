# Stage 1: Build the application using OpenJDK 19 and Gradle
FROM openjdk:21-jdk-slim AS build
WORKDIR /app

# Copy Gradle files
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Copy application source code
COPY src src

# Set execution permission for the Gradle wrapper
RUN chmod +x ./gradlew

# Build the application (without running tests)
RUN ./gradlew build -x test

# Stage 2: Create the final Docker image
FROM openjdk:21-jdk-slim
VOLUME /tmp

# Copy the JAR file from the build stage to the final image
COPY --from=build /app/build/libs/*.jar app.jar

# Set the entry point for the application
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
