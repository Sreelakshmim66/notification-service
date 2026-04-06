# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=NotificationServerApplicationTests
```

The built JAR is at `target/notification-server-0.0.1-SNAPSHOT.jar`.

## Architecture

This is a Spring Boot 3.2 microservice (Java 17) that acts as a notification sink in a larger microservice ecosystem. It registers with a Eureka service discovery server.

**Key configuration** (`application.properties`):
- Runs on port **8084**
- Registers as `notification-service` in Eureka at `http://localhost:8761/eureka/`
- Writes notification files to `./booking-notifications/` (configurable via `app.booking-files-dir`)

**Request flow:**
1. A caller (e.g., an orchestrator service) POSTs to `POST /api/notifications`
2. `NotificationController` validates the request and delegates to `NotificationService`
3. `NotificationService` logs the notification and writes a `.txt` file to the booking-notifications directory named `booking-{itinNumber}-{uuid}.txt`
4. Returns a `NotificationResponse` with the echoed fields plus a `createdAt` timestamp

**DTOs** (`NotificationDtos.java`): Single file containing both request (`CreateNotificationRequest`) and response (`NotificationResponse`) as static inner classes. Required fields: `itinNumber`, `userName`, `message`. Optional: `userEmail`.

**No database** — persistence is file-based only. The `booking-notifications/` directory is created automatically if it doesn't exist.
