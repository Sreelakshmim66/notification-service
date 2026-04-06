package com.notification_server.service;

import com.notification_server.dto.NotificationDtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {

    @Value("${app.booking-files-dir:./booking-notifications}")
    private String bookingFilesDir;

    public NotificationDtos.NotificationResponse send(NotificationDtos.CreateNotificationRequest req) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();

        log.info("[NOTIFICATION] itinNumber={} userName={} message={}",
                req.getItinNumber(), req.getUserName(), req.getMessage());

        writeBookingFile(id, createdAt, req);

        NotificationDtos.NotificationResponse response = new NotificationDtos.NotificationResponse();
        response.setItinNumber(req.getItinNumber());
        response.setUserName(req.getUserName());
        response.setUserEmail(req.getUserEmail());
        response.setMessage(req.getMessage());
        response.setCreatedAt(createdAt.toString());
        return response;
    }

    private void writeBookingFile(String id, LocalDateTime createdAt, NotificationDtos.CreateNotificationRequest req) {
        try {
            Path dir = Paths.get(bookingFilesDir);
            Files.createDirectories(dir);

            Path file = dir.resolve("booking-" + req.getItinNumber() + "-" + id + ".txt");

            String content = "===== BOOKING NOTIFICATION =====\n" +
                    "Itinerary Number : " + req.getItinNumber() + "\n" +
                    "User Name        : " + req.getUserName() + "\n" +
                    "User Email       : " + (req.getUserEmail() != null ? req.getUserEmail() : "N/A") + "\n" +
                    "Message          : " + req.getMessage() + "\n" +
                    "Created At       : " + createdAt + "\n" +
                    "================================\n";

            Files.writeString(file, content);
            log.info("[NOTIFICATION] Booking file written: {}", file.toAbsolutePath());
        } catch (IOException e) {
            log.error("[NOTIFICATION] Failed to write booking file for itinNumber={}: {}", req.getItinNumber(), e.getMessage());
        }
    }
}
