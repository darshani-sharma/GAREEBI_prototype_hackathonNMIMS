# ---- Stage 1: Build ----
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Copy dependency files first for better layer caching
COPY settings.gradle.kts build.gradle.kts ./

# Download dependencies (cached layer)
RUN gradle dependencies --no-daemon || true

# Copy full source and build
COPY src ./src
RUN gradle bootJar --no-daemon -x test

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user for security (run as non-root)
RUN addgroup -S gareebi && adduser -S gareebi -G gareebi && \
    mkdir -p /app && chown -R gareebi:gareebi /app

COPY --from=builder --chown=gareebi:gareebi /app/build/libs/*.jar app.jar

USER gareebi

EXPOSE 8080

# Production-ready JVM flags:
# -XX:+UseContainerSupport: Enable container-aware memory detection (Java 10+)
# -XX:MaxRAMPercentage=75.0: Use up to 75% of container memory for heap
# -XX:+UseParallelGC: Use Parallel GC for better throughput in containers
# -XX:+ExitOnOutOfMemoryError: Exit immediately on OOM (let orchestrator restart)
# -XX:+DisableExplicitGC: Prevent full GC pauses from System.gc()
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseParallelGC", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-XX:+DisableExplicitGC", \
    "-Dspring.profiles.active=prod", \
    "-jar", "app.jar"]
