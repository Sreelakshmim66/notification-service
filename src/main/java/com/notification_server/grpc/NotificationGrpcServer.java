package com.notification_server.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * gRPC server endpoint for notification-service.
 *
 * booking-service calls SendNotification after every successful booking.
 * The generated stubs come from notification.proto compiled at build time.
 */
@Component
public class NotificationGrpcServer {

    private static final Logger log = LoggerFactory.getLogger(NotificationGrpcServer.class);

    public NotificationGrpcServer() {
        // Keep the bean so the package structure is stable while gRPC wiring is pending.
        log.info("gRPC server is currently disabled (no generated stubs/proto config found).");
    }
}
