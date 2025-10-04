# Build stage
FROM gradle:8.14.0-jdk17 AS build
WORKDIR /app

# Copy gradle files
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

# Download dependencies
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build shadow JAR
RUN gradle shadowJar --no-daemon

# Runtime stage
FROM openjdk:17-slim
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/app.jar app.jar

# Copy pets.json (required by HeroService)
COPY pets.json ./pets.json

# Copy entrypoint script
COPY entrypoint.sh ./entrypoint.sh
RUN chmod +x entrypoint.sh

# Expose port 8080
EXPOSE 8080

# Set default environment variables
ENV FIREBASE_SERVICE_ACCOUNT_PATH=/app/firebase-service-account.json

# Run the application via entrypoint
CMD ["./entrypoint.sh"]
